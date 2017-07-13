package br.edu.ifpb.pdm.questao_03;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by natarajan on 27/06/17.
 */

/*
    Classe que usa a biblioteca Retrofit para construir um client rest

    Ver exemplo:
    https://android.jlelse.eu/consuming-rest-api-using-retrofit-library-in-android-ed47aef01ecb

 */
public interface CorreiosRestApiService {

    @GET("{cep}")
    Call<ResultCEP> getCEPDetails(@Path("cep") String cep);

}
