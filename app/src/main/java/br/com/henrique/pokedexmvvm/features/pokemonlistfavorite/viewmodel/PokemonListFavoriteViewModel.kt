package br.com.henrique.pokedexmvvm.features.pokemonlistfavorite.viewmodel

import br.com.henrique.pokedexmvvm.BuildConfig
import br.com.henrique.pokedexmvvm.application.SingleStateViewModel
import br.com.henrique.pokedexmvvm.data.db.database.PokemonDatabase
import br.com.henrique.pokedexmvvm.data.db.model.PokemonEntity
import br.com.henrique.pokedexmvvm.features.pokemondetail.viewstate.PokemonDetailViewState
import br.com.henrique.pokedexmvvm.features.pokemonlistfavorite.viewstate.PokemonListFavoriteViewState
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class PokemonListFavoriteViewModel(
    private val database: PokemonDatabase
) : SingleStateViewModel<PokemonListFavoriteViewState>(),
    CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job
    private var favoriteList: List<PokemonEntity>? = null

    fun getPokemonsFromDatabase() {
        launch {
            database.DAO().getAllPokemons().also {
                favoriteList = it
                emit(PokemonListFavoriteViewState.ShowListFavorite(it))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}