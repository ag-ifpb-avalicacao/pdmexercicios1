package br.edu.ifpb.pdm.questao_05_app.service;

import java.util.List;

import br.edu.ifpb.pdm.questao_05_app.model.MyChatMessage;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by natarajan on 27/06/17.
 */

/*
    Classe que usa a biblioteca Retrofit para construir um client rest

    Ver exemplo:
    https://android.jlelse.eu/consuming-rest-api-using-retrofit-library-in-android-ed47aef01ecb

 */
public interface MessageRestApiService {

    @GET("message/greater/{id}")
    Call<List<MyChatMessage>> getMessagesAfter(@Path("id") String id);

    @POST("message")
    Call<MyChatMessage> saveMessage(@Body MyChatMessage message);

}
