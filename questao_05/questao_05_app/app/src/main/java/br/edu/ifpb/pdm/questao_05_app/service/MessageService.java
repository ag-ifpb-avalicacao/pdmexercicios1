package br.edu.ifpb.pdm.questao_05_app.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import br.edu.ifpb.pdm.questao_05_app.model.MyChatMessage;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by natarajan on 27/06/17.
 */

/*
 * IntentService que realiza comunicação com o servidor de mensagens
 */
public class MessageService extends IntentService {

//    public static final String URL_MESSAGE_API = "http://10.0.3.2:8081/message-server/api/";    //para uso com a versão server docker
    public static final String URL_MESSAGE_API = "https://pdmmessages.herokuapp.com/api/";    //para uso com a versão server online
    private static Retrofit retrofit;

    public MessageService() {
        super("MessageService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MessageService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        // recupera informação vinda na intent que diz qual comando executar
        String command = intent.getStringExtra("command");

        //usando a biblioteca Retrofit para iniciar um client rest simples
        retrofit = new Retrofit.Builder()
                .baseUrl(URL_MESSAGE_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MessageRestApiService apiService = retrofit.create(MessageRestApiService.class);    // cria apiService Retrofit
        //cria localBroadcast para enviar resultados da execução do serviço
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getApplicationContext());

        switch (command) {
            // comando ler mensagens
            case "GET": {
                String id = intent.getStringExtra("id");
                Call<List<MyChatMessage>> call = apiService.getMessagesAfter(id);   // prepara chamada à api
                try {
                    List<MyChatMessage> messages = call.execute().body();   // executa o GET

                    // se houver resultados envia pelo local broacast manager
                    if (messages != null && messages.size() > 0) {

                        // intent que identifica o tratamento que a activity deve dar ao receber esse resultado
                        Intent intentResponse = new Intent("br.edu.ifpb.server.NEW_MESSAGES");

                        intentResponse.putExtra("messages", (Serializable) messages);
                        manager.sendBroadcast(intentResponse);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    manager.sendBroadcast(new Intent("br.edu.ifpb.server.ERRO")); //envia mensagem se houve erro
                }
            } break;

            // comando salvar mensagem
            case "POST": {
                MyChatMessage message = (MyChatMessage) intent.getSerializableExtra("message");
                Call<MyChatMessage> call = apiService.saveMessage(message); // prepara chamada à api

                try {
                    Response<MyChatMessage> responseCall = call.execute();  // chamando o POST

                    // se a mensagem foi devidamente salva/criada
                    if (responseCall.code() == 201) {

                        MyChatMessage messageSaved = responseCall.body();
                        Intent intentResponse = new Intent("br.edu.ifpb.server.SAVED");
                        intentResponse.putExtra("saved", (Serializable) messageSaved);
                        manager.sendBroadcast(intentResponse);  // informa à activity

                    } else {
                        // manda mensagem de erro se a menasgem não salva
                        manager.sendBroadcast(new Intent("br.edu.ifpb.server.ERRO"));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    manager.sendBroadcast(new Intent("br.edu.ifpb.server.ERRO")); //envia mensagem se houve erro
                }
            } break;

        } //end switch

    }
}
