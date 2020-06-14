package com.example.frybl.Networking.Retrofit.Responses;

import com.example.frybl.Model.Quote;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuoteResponse {

    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("author")
    @Expose
    private String author;

    public Quote getQuote()
    {
        return new Quote(content,author);
    }
}
