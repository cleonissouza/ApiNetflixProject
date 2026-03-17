package com.jamiltondamasceno.projetonetflixapi.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RetrofitService {
    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        val retrofit = Retrofit.Builder()
            .baseUrl( BASE_URL )
            .addConverterFactory( GsonConverterFactory.create() )
            .build()

        val filmeAPI = retrofit.create( FilmeAPI::class.java )
    }
}