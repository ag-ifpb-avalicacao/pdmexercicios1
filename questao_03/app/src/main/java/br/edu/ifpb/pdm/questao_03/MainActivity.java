package br.edu.ifpb.pdm.questao_03;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static String CONN_FAILURE;
    private static String WRONG_CEP;

    private Button botaoBuscar;
    private EditText editTextCep;


    public void consultarCEP(){
        final String cepString = editTextCep.getText().toString();

        Intent intent = new Intent(this, ClientCorreiosService.class);
        intent.putExtra("cep", cepString);

        startService(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.CONN_FAILURE = LocalBroadcastsFilterNames.CONNECTION_FAILURE;
        this.WRONG_CEP = LocalBroadcastsFilterNames.WRONG_CEP;

        botaoBuscar = (Button)findViewById(R.id.button);
        editTextCep = (EditText) findViewById(R.id.editText2);


        botaoBuscar.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                consultarCEP();
            }
        });


        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());


        //mostra mensagem quando houve falha tentativa de consultar a APi
        localBroadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Toast.makeText(MainActivity.this, "Falha de conexão. Você pode estar sem conexão ou o serviço de consulta está passando por problemas", Toast.LENGTH_SHORT).show();
            }
        }, new IntentFilter(CONN_FAILURE));


        // mostra mensagem quando o serviço retorna que o CEP está incorreto
        localBroadcastManager.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Toast.makeText(MainActivity.this, "CEP inválido", Toast.LENGTH_SHORT).show();
            }
        }, new IntentFilter(LocalBroadcastsFilterNames.WRONG_CEP));




//        localBroadcastManager.registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//
////                ResultCEP cep = (ResultCEP) intent.getSerializableExtra("cep");
////
////                Intent i = new Intent(context, ResultActivity.class);
////
////                getApplication().startActivity(i);
//
//            }
//        }, new IntentFilter(LocalBroadcastsFilterNames.CEP_OK));

    }




}
