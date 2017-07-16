package br.edu.ifpb.pdm.questao_05_app.presentation;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.edu.ifpb.pdm.questao_05_app.R;
import br.edu.ifpb.pdm.questao_05_app.model.MyChatMessage;

/**
 * Adapter para mostrar a lista de mensagens usando os balões com layouts personsalizados
 * Created by natarajan on 30/06/17.
 */

public class MessageAdapter extends ArrayAdapter<MyChatMessage> {

    private List<MyChatMessage> messages;   // uma lista de mensagens
    private Context context;                // o contexto onde o adapter está / de onde é chamado


    public MessageAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<MyChatMessage> objects) {
        super(context, resource, objects);
        this.context = context;
        this.messages = objects;
    }

    @Override
    public int getCount() {         // quantidade de itens da lista
        if (messages != null) {
            return messages.size();
        } else {
            return 0;
        }
    }

    @Nullable
    @Override
    // método para pegar uma mensagem segundo a posição na lista
    public MyChatMessage getItem(int position) {
        if (messages != null) {
            return messages.get(position);
        } else {
            return null;
        }
    }

    @NonNull
    @Override
    /**
     * Método que personaliza cada mensagem no ListView
     */
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // setando variáveis necessárias
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        int layoutResource = 0; // determined by view type
        MyChatMessage chatMessage = getItem(position);
        int viewType = getItemViewType(position);
        Activity contextActivity = (Activity) this.context;

        // usa informação que vem da intent do contexto onde o adapter está para saber se é uma tela
        // para a Pessoa 1 ou 2
        boolean isPerson1 = contextActivity.getIntent().getBooleanExtra("isPerson1", true);


        // setando o layout dos balões dependendo da pessoa que vai ver as mensagens
        if (chatMessage.isPerson1() == isPerson1) {
            layoutResource = R.layout.chat_message_layout_left;
        } else {
            layoutResource = R.layout.chat_message_layout_right;
        }

        // colocando a mensagem no balão
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        holder.msg.setText(chatMessage.getContent());

        return convertView;
    }

    @Override
    // retorna a quantidade de tipos de view que esse adapter tem
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return IGNORE_ITEM_VIEW_TYPE;       // informa que o tipo de view independe da posição do item na lista
    }

    // classe interna utilitária para configuração
    private class ViewHolder {
        private TextView msg;

        public ViewHolder(View v) {
            msg = (TextView) v.findViewById(R.id.txt_msg);
        }
    }

    // método para retornar mensagnes contidas no adapter
    public List<MyChatMessage> getMessages() {
        return messages;
    }
}
