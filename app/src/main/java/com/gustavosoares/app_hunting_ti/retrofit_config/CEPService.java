package com.gustavosoares.app_hunting_ti.retrofit_config;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CEPService {

    @GET("{cep}/sjon")
    Call<CEP> buscarCEP(@Path("cep") String cep);

}
