package com.example.frybl.Networking.Retrofit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.frybl.Model.Quote;
import com.example.frybl.Networking.Retrofit.API.QuoteApi;
import com.example.frybl.Networking.Retrofit.Responses.QuoteResponse;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiManager {

    private QuoteApi quoteApi;
    private MutableLiveData<Quote> quoteLiveData;

    @Inject
    public ApiManager(QuoteApi quoteApi)
    {
        this.quoteApi = quoteApi;
        this.quoteLiveData = new MutableLiveData<>();
    }

    public void requestQuote()
    {
        Call<QuoteResponse> call = quoteApi.getQuote();
        call.enqueue(new Callback<QuoteResponse>() {
            @Override
            public void onResponse(Call<QuoteResponse> call, Response<QuoteResponse> response) {
                if (response.code() == 200)
                {
                    quoteLiveData.setValue(response.body().getQuote());
                }
            }
            @Override
            public void onFailure(Call<QuoteResponse> call, Throwable t) {

            }
        });
    }

    public LiveData<Quote> getQuoteLiveData()
    {
        return quoteLiveData;
    }


}
