package br.edu.ifpb.pdm.questao_05_app.presentation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.edu.ifpb.pdm.questao_05_app.R;

/**
 * Main Activity
 */
public class MainActivity extends AppCompatActivity {

    private Button botaoPessoa1;    // botão para ir para tela de pessoa 1
    private Button botaoPessoa2;    // mesma coisa, para pessoa 2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // pegando referencias dos widgets
        botaoPessoa1 = (Button) findViewById(R.id.button);
        botaoPessoa2 = (Button) findViewById(R.id.button2);


        // atribuindo ação de abrir chat da Pessoa 1
        botaoPessoa1.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                MainActivity.this.mostraTelaPessoa1();
            }
        });

        // atribuindo ação de abrir chat da Pessoa 2
        botaoPessoa2.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                MainActivity.this.mostraTelaPessoa2();
            }
        });

    }

    // chama tela de pessoa 1
    private void mostraTelaPessoa1() {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("isPerson1", true);
        startActivity(intent);
    }

    // chama tela de pessoa 2
    private void mostraTelaPessoa2() {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("isPerson1", false);
        startActivity(intent);
    }


}
