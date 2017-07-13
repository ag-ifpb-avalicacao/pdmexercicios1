package br.edu.ifpb.pdm.questao_01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
//import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Tela1Activity extends AppCompatActivity {

    private ListView listView;      // list view que carrega os afazeres/tarefas
    private EditText editText;      // edittext para inserir text de uma tarefa
    private Button addButton;       // botão para add tarefa
    private static Bundle mySavedInstanceState;    // Bundle para salvar estados em processo de transição no ciclo de vida
    private ToDoAdapter adapter;    // adapter para manipular o listview

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);

        // setando o layout do adapter e linkando-o com o listview
        listView = (ListView) findViewById(R.id.toDoListView);
        adapter = new ToDoAdapter(this, R.layout.todo_layout, new ArrayList<String>());
        listView.setAdapter(adapter);

        // caso a tela tenha sido recriada, carrega as informações salvas na variável mySavedInstanceState
        if (mySavedInstanceState == null) {
            mySavedInstanceState = new Bundle();
        } else {
            List<String> todos = (List<String>) mySavedInstanceState.getSerializable("toDos");
            adapter.addAll(todos);
            adapter.notifyDataSetChanged();
        }

        // setando função de adicionar tarefa
        addTodo();
    }


    // código usado para adicionar uma tarefa quando o botão add é pressionado
    private void addTodo() {

        editText = (EditText) findViewById(R.id.editText);
        addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String toDoStringText = editText.getText().toString();
                adapter.add(toDoStringText);
                adapter.notifyDataSetChanged();
                editText.setText(null);
                listView.setSelection(adapter.getCount() - 1);

            }
        });
    }


    // métodos adicionados para suportar salvar as tarefas quando ocorre mudança de tela ou rotação.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(mySavedInstanceState);
        // salva as tarefas na variável mySavedInstance
        mySavedInstanceState.putSerializable("toDos", (Serializable) adapter.getToDos());
    }

    @Override
    protected void onPause() {
        onSaveInstanceState(mySavedInstanceState);  // antes de pausar o aplicativo, pede para guardar as tarefas
        super.onPause();
    }

}
