package com.example.frybl.Networking.Retrofit.API;

import com.example.frybl.Networking.Retrofit.Responses.QuoteResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface QuoteApi {
    @GET("/random")
    Call<QuoteResponse> getQuote();
}
