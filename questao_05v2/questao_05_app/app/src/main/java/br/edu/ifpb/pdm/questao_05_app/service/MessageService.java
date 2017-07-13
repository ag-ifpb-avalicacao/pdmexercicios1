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
 * IntentService que realiza a consulta
 */
public class MessageService extends IntentService {

//    public static final String URL_MESSAGE_API = "http://10.0.3.2:8081/message-server/api/";    //para uso com a versão server docker
    public static final String URL_MESSAGE_API = "https://pdmmessages.herokuapp.com/api/";    //para uso com a versão server online
    private static Retrofit retrofit = null;

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


        //
        String command = intent.getStringExtra("command");

        //usando a biblioteca Retrofit para iniciar um client rest simples
        retrofit = new Retrofit.Builder()
                .baseUrl(URL_MESSAGE_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MessageRestApiService apiService = retrofit.create(MessageRestApiService.class);
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getApplicationContext());

        switch (command) {
            //
            case "GET": {
                String id = intent.getStringExtra("id");
                Call<List<MyChatMessage>> call = apiService.getMessagesAfter(id);
                try {
                    List<MyChatMessage> messages = call.execute().body();
                    if (messages != null && messages.size() > 0) {
                        Intent intentResponse = new Intent("br.edu.ifpb.server.NEW_MESSAGES");

                        intentResponse.putExtra("messages", (Serializable) messages);
                        manager.sendBroadcast(intentResponse);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    manager.sendBroadcast(new Intent("br.edu.ifpb.server.ERRO"));
                }
            } break;

            //
            case "POST": {
                MyChatMessage message = (MyChatMessage) intent.getSerializableExtra("message");
                Call<MyChatMessage> call = apiService.saveMessage(message);

                try {
                    Response<MyChatMessage> responseCall = call.execute();

                    if (responseCall.code() == 201) {

                        MyChatMessage messageSaved = responseCall.body();
                        Intent intentResponse = new Intent("br.edu.ifpb.server.SAVED");
                        intentResponse.putExtra("saved", (Serializable) messageSaved);
                        manager.sendBroadcast(intentResponse);

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    manager.sendBroadcast(new Intent("br.edu.ifpb.server.ERRO"));
                }
            } break;

        } //end switch

    }
}
