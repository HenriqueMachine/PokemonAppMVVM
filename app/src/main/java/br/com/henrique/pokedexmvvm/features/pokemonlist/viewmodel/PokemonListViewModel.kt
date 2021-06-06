package br.com.henrique.pokedexmvvm.features.pokemonlist.viewmodel

import br.com.henrique.pokedexmvvm.BuildConfig
import br.com.henrique.pokedexmvvm.application.SingleStateViewModel
import br.com.henrique.pokedexmvvm.data.model.CustomResponse
import br.com.henrique.pokedexmvvm.features.pokemonlist.repository.PokemonListService
import br.com.henrique.pokedexmvvm.features.pokemonlist.viewstate.PokemonListViewState
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class PokemonListViewModel(
    private val repository: PokemonListService) : SingleStateViewModel<PokemonListViewState>(),
    CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job
    private var pathNextList = ""

    fun getList() {
        launch {
            emit(PokemonListViewState.ShowLoading(true))
            val result = withContext(Dispatchers.IO) {
                repository.getListPokemon()
            }

            when (result) {
                is CustomResponse.Success -> {
                    emit(PokemonListViewState.ShowPokemonList(result.data.results))
                    pathNextList = result.data.next.removePrefix(BuildConfig.BASE_POKEDEX_URL)
                    emit(PokemonListViewState.ShowLoading(false))
                }
                is CustomResponse.Error -> {
                    emit(PokemonListViewState.ShowError)
                }
            }
        }
    }

    fun getNextPage() {
        launch {
            emit(PokemonListViewState.ShowLoading(true))
            val result = withContext(Dispatchers.IO) {
                repository.getNextPage(pathNextList)
            }

            when (result) {
                is CustomResponse.Success -> {
                    emit(PokemonListViewState.LoadNextPage(result.data.results))
                    pathNextList = result.data.next.removePrefix(BuildConfig.BASE_POKEDEX_URL)
                    emit(PokemonListViewState.ShowLoading(false))
                }
                is CustomResponse.Error -> {
                    emit(PokemonListViewState.ShowError)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}