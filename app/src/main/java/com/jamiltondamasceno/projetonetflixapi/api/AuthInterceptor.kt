package com.jamiltondamasceno.projetonetflixapi.api

import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.http.Url

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        // 1) Acessar requisicao
        val construtorRequisicao = chain.request().newBuilder()

        // 2) Alterar URl ou Rota da Requisicao
        // https://api.themoviedb.org/3/ + movie/latest (urlAtual)
        // https://api.themoviedb.org/3/ + movie/latest + ap1_key (novaUrl)
        val urlAtual = chain.request().url()
        val novaUrl = urlAtual.newBuilder()
        novaUrl.addQueryParameter(
            "api_key",
            RetrofitService.API_KEY
        )



        // Configurar novaUrl na requisicao
        construtorRequisicao.url(novaUrl.build())

        return chain.proceed(construtorRequisicao.build()) //Response
    }
}