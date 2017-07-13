package br.edu.ifpb.pdm.questao_01;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;


/**
 * Adapter para mostrar a lista de toDo's / tarefas
 * Created by natarajan on 30/06/17.
 */

public class ToDoAdapter extends ArrayAdapter<String> {

    private List<String> toDos;   // uma lista de mensagens
    private Context context;      // o contexto onde o adapter está / de onde é chamado


    public ToDoAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.toDos = objects;
    }

    @Override
    public int getCount() {         // quantidade de itens da lista
        if (toDos != null) {
            return toDos.size();
        } else {
            return 0;
        }
    }

    @Nullable
    @Override
    // método para pegar uma mensagem segundo a posição na lista
    public String getItem(int position) {
        if (toDos != null) {
            return toDos.get(position);
        } else {
            return null;
        }
    }

    @NonNull
    @Override
    /**
     * Método que personaliza cada item no ListView e seus comportamentos
     */
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // setando variáveis necessárias
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        String toDoText = getItem(position);    //pegando o texto de uma tarefa em específico
        int viewType = getItemViewType(position);   // e sua posição


        // colocando a mensagem no textView do layout
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(R.layout.todo_layout, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        holder.msg.setText(toDoText);


        // Adicionando o botão Delete e setando a sua função de apagar a tarefa quando é clicado
        Button deleteButton = (Button) convertView.findViewById(R.id.button4);
        deleteButton.setTag(position);

        deleteButton.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toDos.remove(position);
                        notifyDataSetChanged();
                    }
                }
        );

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
        private Button deleteButton;

        public ViewHolder(View v) {
            msg = (TextView) v.findViewById(R.id.todoTextView);
            deleteButton = (Button) v.findViewById(R.id.button4);

        }
    }

    // método para retornar mensagnes contidas no adapter
    public List<String> getToDos() {
        return toDos;
    }
}
