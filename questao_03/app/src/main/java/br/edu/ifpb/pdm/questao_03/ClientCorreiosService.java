package br.edu.ifpb.pdm.questao_03;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import java.io.IOException;
import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by natarajan on 27/06/17.
 */

/*
 * IntentService que realiza a consulta
 */
public class ClientCorreiosService extends IntentService {

    public static final String URL_API_CORREIOS = "http://correiosapi.apphb.com/cep/";
    private static Retrofit retrofit = null;

    public ClientCorreiosService() {
        super("ClientCorreiosService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ClientCorreiosService(String name) {
        super(name);
    }

    public void consulta() {

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {


        //pega o no CEP informado da MainActivity
        String cep = intent.getStringExtra("cep");

        //usando a biblioteca Retrofit para iniciar um client rest simples
        retrofit = new Retrofit.Builder()
                .baseUrl(URL_API_CORREIOS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CorreiosRestApiService apiService = retrofit.create(CorreiosRestApiService.class);
        Call<ResultCEP> call = apiService.getCEPDetails(cep);


        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getApplicationContext());

        // executa a consulta à API dos Correios
        try {
            final Response<ResultCEP> response = call.execute();

            // resposta 404 quando o CEP é incorreto.
            // manda (via BroadCastManager) a MainActive mostrar uma mensagem
            if (response.code() == 404) {

                manager.sendBroadcast(new Intent(LocalBroadcastsFilterNames.WRONG_CEP));

            } else {

                // consultou um CEP válido
                // entrega informações para ResultActivity

                Intent intentResult = new Intent(getBaseContext(), ResultActivity.class);
                intentResult.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentResult.putExtra("cep", (Serializable) response.body());
                getApplication().startActivity(intentResult);


//                Intent intentResult = new Intent(LocalBroadcastsFilterNames.CEP_OK);
//                intentResult.putExtra("cep", (Serializable) response.body());
//                manager.sendBroadcast(intentResult);


            }

        } catch (IOException e) {
            e.printStackTrace();
            //se houve erro na tentativa de chamar a API
            // manda (via BroadCastManager) a MainActive mostrar uma mensagem
            manager.sendBroadcast(new Intent(LocalBroadcastsFilterNames.CONNECTION_FAILURE));
        }


    }
}
