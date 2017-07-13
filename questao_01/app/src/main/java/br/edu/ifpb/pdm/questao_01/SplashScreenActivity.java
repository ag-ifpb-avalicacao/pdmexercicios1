package br.edu.ifpb.pdm.questao_01;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

/**
 * Activity da SplashScreen mostrada no início do aplicativo
 */
public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);

        /*
           O Handler aqui é utilizado para fazer o aplicativo mostrar a splash screen durante
           2 segundos e depois carrega a tela principal
         */
        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                mostrarTelaPrincipal();
            }
        }, 2000);
    }


    /*
        método que chama a tela principal e depois finaliza a splash screen para que ela não fique
        na pilha de execução
     */
    private void mostrarTelaPrincipal() {
        Intent intent = new Intent(SplashScreenActivity.this,
                MainActivity.class);
        startActivity(intent);
        finish();
    }
}
