package br.edu.ifpb.pdm.questao_03;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;


/**
 * Activity para mostrar os resultados válidos das buscas
 */
public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //mostrando o resultado
        ResultCEP cep = (ResultCEP) getIntent().getSerializableExtra("cep");

        TextView textViewResult  = (TextView) findViewById(R.id.textView);
        textViewResult.setText(cep.getCep());

        TextView textViewLogradouro  = (TextView) findViewById(R.id.textView5);
        textViewLogradouro.setText(cep.getTipoDeLogradouro() + " " + cep.getLogradouro());

        TextView textViewBairro  = (TextView) findViewById(R.id.textView6);
        textViewBairro.setText(cep.getBairro());

        TextView textViewCidadeUf  = (TextView) findViewById(R.id.textView7);
        textViewCidadeUf.setText(cep.getCidade() + " - " + cep.getEstado());


        //Butão consultar outro CEP
        Button btn = (Button) findViewById(R.id.btnback);
        btn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
            finish();
            }
        });


    }
}
