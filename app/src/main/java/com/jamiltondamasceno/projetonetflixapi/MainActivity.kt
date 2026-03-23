package com.jamiltondamasceno.projetonetflixapi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jamiltondamasceno.projetonetflixapi.adapter.FilmeAdapter
import com.jamiltondamasceno.projetonetflixapi.api.FilmeAPI
import com.jamiltondamasceno.projetonetflixapi.api.RetrofitService
import com.jamiltondamasceno.projetonetflixapi.databinding.ActivityMainBinding
import com.jamiltondamasceno.projetonetflixapi.model.FilmeRecente
import com.jamiltondamasceno.projetonetflixapi.model.FilmeResposta
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val TAG = "info_filme"
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val filmeAPI by lazy {
        RetrofitService.filmeAPI
    }

    private lateinit var filmeAdapter: FilmeAdapter

    var jobFilmeRecente: Job? = null
    var jobFilmesPopulares: Job? = null
    var linearLayoutManager: LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        inicializarViews()

    }

    private fun inicializarViews() {

        filmeAdapter = FilmeAdapter{ filme ->
            val intent = Intent(this, DetalhesActivity::class.java)
            intent.putExtra("filme", filme)
            startActivity(intent)
        }
        binding.rvPopulares.adapter = filmeAdapter

        linearLayoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )

        binding.rvPopulares.layoutManager = linearLayoutManager

        binding.rvPopulares.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                //0..19 (20)
                val ultimoItemVisivel = linearLayoutManager?.findLastVisibleItemPosition()
                val totalItens = recyclerView.adapter?.itemCount
                //Log.i("recycler_test", "ultimo: $ultimoItemVisivel total: $totalItens")

                if (ultimoItemVisivel != null && totalItens != null){
                    if (totalItens - 1 == ultimoItemVisivel){//chegou no ultimo item
                        binding.fabAdicionar.hide()
                    }else{//nao chegou no ultimo item
                        binding.fabAdicionar.show()
                    }
                }
                /*Log.i("recycler_test", "onScrolled: dx: $dx dy: $dy")
                if (dy > 0){//descendo
                    binding.fabAdicionar.hide()
                }else{//subindo
                    binding.fabAdicionar.show()
                }*/
            }
        })
    }

  /*  class ScrollCustomizado : RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

        }
    }*/

    override fun onStart() {
        super.onStart()
        recuperarFilmeRecente()
        recuperarFilmesPopulares()
    }

    private fun recuperarFilmesPopulares() {
        jobFilmesPopulares = CoroutineScope(Dispatchers.IO).launch {
            var resposta: Response<FilmeResposta>? = null

            try {
                resposta = filmeAPI.recuperarFilmesPopulares()
            } catch (e: Exception) {
                exibirMensagem("Erro ao fazer a requisicao")
            }

            if (resposta != null) {
                if (resposta.isSuccessful) {

                    val filmeResposta = resposta.body()
                    val listaFilmes = filmeResposta?.filmes
                    if (listaFilmes != null && listaFilmes.isNotEmpty()){

                       /* Log.i("filmes_api", "lista Filmes: ")
                        listaFilmes.forEach { filme ->
                            Log.i("filmes_api", "Titulo: ${filme.title} ")
                        }*/

                        withContext(Dispatchers.Main){

                            filmeAdapter.adicionarLista(listaFilmes)

                        }


                    }



                } else {
                    exibirMensagem("Problema ao fazer a requisicao CODE: ${resposta.code()}")
                }
            } else {
                exibirMensagem("Nao foi possivel fazer a requisicao")
            }
        }
    }

    private fun recuperarFilmeRecente() {
        jobFilmeRecente = CoroutineScope(Dispatchers.IO).launch {
            var resposta: Response<FilmeRecente>? = null

            try {
                resposta = filmeAPI.recuperarFilmeRecente()
            } catch (e: Exception) {
                exibirMensagem("Erro ao fazer a requisicao")
            }

            if (resposta != null) {
                if (resposta.isSuccessful) {

                    val filmeRecente = resposta.body()
                    val nomeImagem = filmeRecente?.poster_path
                    //val titulo = filmeRecente?.title
                    val url = RetrofitService.BASE_URL_IMAGEM + "w780" + nomeImagem

                    withContext(Dispatchers.Main) {

                       /* val texto = "titulo: $titulo url: $url"
                        binding.textPopulares.text = texto*/
                        Picasso.get()
                            .load(url)
                            .error(R.drawable.capa)
                            .into(binding.imgCapa)
                    }

                } else {
                    exibirMensagem("Problema ao fazer a requisicao CODE: ${resposta.code()}")
                }
            } else {
                exibirMensagem("Nao foi possivel fazer a requisicao")
            }
        }
    }

    private fun exibirMensagem(mensagem: String) {

        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(
                applicationContext,
                mensagem,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onStop() {
        super.onStop()
        jobFilmeRecente?.cancel()
        jobFilmesPopulares?.cancel()
    }
}