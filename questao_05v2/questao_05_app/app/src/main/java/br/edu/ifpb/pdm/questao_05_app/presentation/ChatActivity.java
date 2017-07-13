package br.edu.ifpb.pdm.questao_05_app.presentation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifpb.pdm.questao_05_app.R;
import br.edu.ifpb.pdm.questao_05_app.model.MyChatMessage;
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
    private Bundle mySavedInstanceState;    // Bundle para salvar estados em processo de transição no ciclo de vida
    private boolean isPerson1;              // boolean que guarda a informação se é a pessoa 1 ou 2 (vindo do Intent)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //guarda Bundle
        mySavedInstanceState = savedInstanceState;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);


        // pegando componentes
        messageEditText = (EditText) findViewById(R.id.editText);
//        sendButton = (Button) findViewById(R.id.button3);
        sendButton = (ImageButton) findViewById(R.id.button3);
        messagesListView = (ListView) findViewById(R.id.messagesListView);


        // pegando informação se é pessoa 1 ou 2 (pelos intent)
        this.isPerson1 = getIntent().getBooleanExtra("isPerson1", true);

        // chama setting Toolbar
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
                messageEditText.setText("");
                List<MyChatMessage> news = (List<MyChatMessage>) intent.getSerializableExtra("messages");
                adapter.addAll(news);

                adapter.notifyDataSetChanged();
                messagesListView.setSelection(adapter.getCount());

            }
        }, new IntentFilter("br.edu.ifpb.server.NEW_MESSAGES"));

        //
        localBroadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Toast.makeText(getApplicationContext(), "Erro ao tentar conectar com servidor", Toast.LENGTH_LONG).show();

            }
        }, new IntentFilter("br.edu.ifpb.server.ERRO"));

    }

    // Setando a toolbar
    private void setToolbar() {

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

        MaterialLetterIcon mli = (MaterialLetterIcon) findViewById(R.id.letterIcon);
        TextView toolbarTextView = (TextView) findViewById(R.id.toolbarText);

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
    private void updateMessages() {

        int totalMessages = 0;
        totalMessages = messages.size();

        Intent intent = new Intent(this, MessageService.class);
        intent.putExtra("command", "GET");
        intent.putExtra("id", Integer.toString(totalMessages));
//        boolean isPerson1 = getIntent().getBooleanExtra("isPerson1", true);
        startService(intent);

    }


    /*
     * Método para enviar mensagem ao servidor
     */
    private void sendMessage() {
        String messageText = messageEditText.getText().toString();

        Intent intent = new Intent(this, MessageService.class);
        intent.putExtra("command", "POST");

        boolean isPerson1 = getIntent().getBooleanExtra("isPerson1", true);

        intent.putExtra("message", new MyChatMessage(messageText, isPerson1));
        startService(intent);
    }

    //métodos para guardar informações da tela entre reativações da tela
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("messages", (Serializable) adapter.getMessages());
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            List<MyChatMessage> messages = (List<MyChatMessage>) savedInstanceState.getSerializable("messages");
            adapter.addAll(messages);
        }

        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        updateMessages();   // quando a tela é chamada novamente, chama método para buscar novas mensagens no servidor
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public boolean isPerson1() {
        return isPerson1;
    }


}
