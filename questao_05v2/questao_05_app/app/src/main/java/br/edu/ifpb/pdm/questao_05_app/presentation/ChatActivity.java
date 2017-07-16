package br.edu.ifpb.pdm.questao_05_app.presentation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ivbaranov.mli.MaterialLetterIcon;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifpb.pdm.questao_05_app.R;
import br.edu.ifpb.pdm.questao_05_app.model.MyChatMessage;
import br.edu.ifpb.pdm.questao_05_app.service.LocalFileUtils;
import br.edu.ifpb.pdm.questao_05_app.service.MessageService;

/**
 * Classe que descreve a tela de chat
 */
public class ChatActivity extends AppCompatActivity {

    private EditText messageEditText;       // EditText onde vai a mensage
    private ImageButton sendButton;         // Botão usado para enviar a mensagem. Contém um icone;

    private ListView messagesListView;      // widget que engloba as mensagens
    private List<MyChatMessage> messages;   // lista de mensagens
    private MessageAdapter adapter;         // adapter para usar no ListView
    private boolean isPerson1;              // boolean que guarda a informação se é a pessoa 1 ou 2 (vindo do Intent)

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);

        // pegando informação se é pessoa 1 ou 2 (pelos intent)
        this.isPerson1 = getIntent().getBooleanExtra("isPerson1", true);

        // pegando componentes
        messageEditText = (EditText) findViewById(R.id.editText);
        sendButton = (ImageButton) findViewById(R.id.button3);
        messagesListView = (ListView) findViewById(R.id.messagesListView);


        // setando a Toolbar (que contém o icone e nome da Pessoa1/Pessoa2)
        setToolbar();

        // setando ListView das mensagens
        messages = new ArrayList<>();
        adapter = new MessageAdapter(this, R.layout.chat_message_layout_left, messages);
        messagesListView.setAdapter(adapter);


        // setando listener de clique no botão de enviar mensagem
        sendButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });


        // usa LocalBroadCastManager para receber resultados das consultas ao IntentService
        // que se comunica com o servidor das mensagens
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());

        // mostra resultado depois que a mensagem é enviada e salva pelo servidor
        localBroadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                messageEditText.setText("");
                MyChatMessage saved = (MyChatMessage) intent.getSerializableExtra("saved");
                adapter.add(saved);
                adapter.notifyDataSetChanged();
                messagesListView.setSelection(adapter.getCount());
            }
        }, new IntentFilter("br.edu.ifpb.server.SAVED"));

        // mostra resultado quando solicita receber novas mensagens
        localBroadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                List<MyChatMessage> news = (List<MyChatMessage>) intent.getSerializableExtra("messages");
                loadMessagesOnListView(news);

            }
        }, new IntentFilter("br.edu.ifpb.server.NEW_MESSAGES"));

        // mostra mensagem de erro de conexão com o servidor
        localBroadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Toast.makeText(getApplicationContext(), "Erro ao tentar conectar com servidor", Toast.LENGTH_LONG).show();

            }
        }, new IntentFilter("br.edu.ifpb.server.ERRO"));

    }

    /*
        método responsável por carregar mensagens para a listView
     */
    private void loadMessagesOnListView(List<MyChatMessage> messages) {
        this.adapter.addAll(messages);
        this.messagesListView.setSelection(adapter.getCount());
        this.adapter.notifyDataSetChanged();
        messagesListView.smoothScrollByOffset(messages.size());
    }

    // Setando a toolbar
    private void setToolbar() {

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar); //a toolbar personalizada da activity
        setSupportActionBar(toolBar);

        MaterialLetterIcon mli = (MaterialLetterIcon) findViewById(R.id.letterIcon);    // o icone com as iniciais da pessoa
        TextView toolbarTextView = (TextView) findViewById(R.id.toolbarText);       // o nome da pessoa

        //setando
        String toolbarText;
        if (isPerson1()) {
            toolbarText = "Pessoa 1";
        } else {
            toolbarText = "Pessoa 2";
        }
        mli.setLetter(toolbarText);
        toolbarTextView.setText(toolbarText);
    }


    /*
     * Método para pegar as mensagens no servidor
     */
    private void getNewMessages() {

        long lastMessageId = 0;
        // se houver mensagens no adapter, pega o id da última mensagem
        if (messages != null && messages.size() > 0) {
            MyChatMessage myChatMessage = messages.get(messages.size() - 1);
            lastMessageId = myChatMessage.getId();
        }

        // manda buscar pelas mensagens que chegaram e tem id > lastMessageId
        Intent intent = new Intent(this, MessageService.class);
        intent.putExtra("command", "GET");
        intent.putExtra("id", Long.toString(lastMessageId));
        startService(intent);

    }


    /*
     * Método para enviar mensagem ao servidor
     */
    private void sendMessage() {
        String messageText = messageEditText.getText().toString();  // pega a mensagem no editText

        if (messageText != null && !messageText.trim().equals("")) {    // só permite enviar a mensagem se for válida
            messageEditText.setText("");
            // chama IntentService para salvar a mensagem no servidor
            Intent intent = new Intent(this, MessageService.class);
            intent.putExtra("command", "POST");     // tipo da operação
            intent.putExtra("message", new MyChatMessage(messageText, isPerson1()));    // mensagem
            startService(intent);   // chama o MessageService
        } else {
            // se não há mensagem válida mostra mensagem de erro
            Toast.makeText(getApplicationContext(), "Insira uma mensagem", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        /*
            tenta recuperar as mensagens já baixadas e salvas em arquivo no celular
         */
        List<MyChatMessage> savedMessages = LocalFileUtils.readMessagesFile(isPerson1());
        if (savedMessages != null)  // se houver mensagens salvas, carrega na listview
            loadMessagesOnListView(savedMessages);

        /*
             chama método para buscar novas mensagens no servidor. Como é executado assincronamente,
             a activity é iniciada com as mensagens salvas e quando chegam as novas mensagens a tela
             é atualizada
        */
        getNewMessages();

    }

    @Override
    protected void onPause() {
        super.onPause();
        /*
            salva as mensagens atuais no mensagens atuais em arquivo, para serem posteriormente recuperadas
            evitando ter que buscar todas novamente
         */
        LocalFileUtils.saveMessagesOnFile(adapter.getMessages(), isPerson1());
    }

    /*
        método usado para devolver o boolean que informa se a activity criada é de Pessoa1 ou Pessoa2
        true = Pessoa1
        false = Pessoa2
     */
    public boolean isPerson1() {
        return this.isPerson1;
    }

}
