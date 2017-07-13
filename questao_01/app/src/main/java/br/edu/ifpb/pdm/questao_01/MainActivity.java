package br.edu.ifpb.pdm.questao_01;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.Serializable;

/**
 * Casse usada para mostrar a tela principal que faz navegação entre as funções do aplicativo
 */
public class MainActivity extends AppCompatActivity {

    private Button botaoTela1;  // botão 'ir para toDos / tarefas'
    private Button botaoTela2;  // botão 'ir para horário IFPB'

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setando onclick no botão 1 (ir para tela de lista de afazeres)
        botaoTela1 = (Button) findViewById(R.id.button);
        botaoTela1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Tela1Activity.class);
                startActivity(intent);
            }
        });

        //setando onclick no botão 2 (ir horário do ifpb)
        botaoTela2 = (Button) findViewById(R.id.button2);
        botaoTela2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Uri uri = Uri.parse("http://horarios.ifpb.edu.br/cajazeiras");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

    }

}
