package com.jamiltondamasceno.projetonetflixapi.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RetrofitService {
    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val BASE_URL_IMAGEM = "https://image.tmdb.org/t/p/"
        const val API_KEY = "488bda8ab7c0687a374d456d71957a1d"
        val retrofit = Retrofit.Builder()
            .baseUrl( BASE_URL )
            .addConverterFactory( GsonConverterFactory.create() )
            .build()

        val filmeAPI = retrofit.create( FilmeAPI::class.java )
    }
}