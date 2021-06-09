package com.gustavosoares.app_hunting_ti.retrofit_config;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitConfig {

    private final Retrofit retrofit;

    public RetrofitConfig(){

        this.retrofit = new Retrofit.Builder()
                .baseUrl("https://viacep.com.br/ws/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    public CEPService getCEPService(){
        return this.retrofit.create(CEPService.class);
    }

}
