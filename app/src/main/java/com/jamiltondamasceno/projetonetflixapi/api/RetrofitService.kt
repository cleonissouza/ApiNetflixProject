package com.jamiltondamasceno.projetonetflixapi.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitService {
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val BASE_URL_IMAGEM = "https://image.tmdb.org/t/p/"
    const val API_KEY = "488bda8ab7c0687a374d456d71957a1d"

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .writeTimeout(10, TimeUnit.SECONDS) //Escrita (Salvando na API)
        .readTimeout(20, TimeUnit.SECONDS) //Leitura (Recuperando dados da API)
        .connectTimeout(20, TimeUnit.SECONDS) //tempo de conexao maxima
        .addInterceptor(AuthInterceptor())
        .build()
    val retrofit = Retrofit.Builder()
        .baseUrl( BASE_URL )
        .addConverterFactory( GsonConverterFactory.create() )
        .client(okHttpClient)
        .build()

    val filmeAPI: FilmeAPI = retrofit.create( FilmeAPI::class.java )
}