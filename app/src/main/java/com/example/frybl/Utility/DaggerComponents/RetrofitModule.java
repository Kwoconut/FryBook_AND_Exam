package com.example.frybl.Utility.DaggerComponents;

import com.example.frybl.Networking.Retrofit.API.QuoteApi;
import com.example.frybl.Networking.Retrofit.ApiManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RetrofitModule {

    private Retrofit retrofit;

    public RetrofitModule()
    {
        retrofit = new Retrofit.Builder().baseUrl("http://api.quotable.io").addConverterFactory(GsonConverterFactory.create()).build();
    }

    @Singleton
    @Provides
    Retrofit provideRetrofitInstance()
    {
        return retrofit;
    }

    @Singleton
    @Provides
    QuoteApi provideQuoteApi()
    {
        return retrofit.create(QuoteApi.class);
    }

    @Singleton
    @Provides
    ApiManager provideApiManager(QuoteApi quoteApi)
    {
        return new ApiManager(quoteApi);
    }
}
