package br.com.henrique.pokedexmvvm.features.pokemondetail.viewmodel

import br.com.henrique.pokedexmvvm.BuildConfig
import br.com.henrique.pokedexmvvm.application.SingleStateViewModel
import br.com.henrique.pokedexmvvm.data.db.database.PokemonDatabase
import br.com.henrique.pokedexmvvm.data.db.model.PokemonEntity
import br.com.henrique.pokedexmvvm.data.model.CustomResponse
import br.com.henrique.pokedexmvvm.features.pokemondetail.viewstate.PokemonDetailViewState
import br.com.henrique.pokedexmvvm.features.pokemonlist.repository.PokemonListService
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class PokemonDetailViewModel(
    private val repository: PokemonListService,
    private val database: PokemonDatabase
) : SingleStateViewModel<PokemonDetailViewState>(),
    CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job
    private var favoriteList: List<PokemonEntity>? = null

    fun getDetail(url: String) {
        launch {
            val result = withContext(Dispatchers.IO) {
                val detail = url.removePrefix(BuildConfig.BASE_POKEDEX_URL)
                repository.getDetailPokemon(detail)
            }

            when (result) {
                is CustomResponse.Success -> {
                    emit(PokemonDetailViewState.ShowDetailPokemon(result.data))
                }
                is CustomResponse.Error -> {
                }
            }
        }
    }

    fun insertPokemon(pokemon: PokemonEntity) {
        launch {
            database.DAO().insertPokemon(pokemon)
        }
    }

    fun removePokemon(pokemon: PokemonEntity) {
        launch {
            database.DAO().deletePokemon(pokemon)
        }
    }

    fun getPokemonsFromDatabase() {
        launch {
            database.DAO().getAllPokemons().also {
                favoriteList = it
                emit(PokemonDetailViewState.GetFavoritePokemons(it))
            }
        }
    }

    fun checkPokemonIsFavorite(pokemonId: Int) {
        launch {
            val favorite = favoriteList?.firstOrNull { pokemonId == it.id }
            favorite?.let {
                emit(PokemonDetailViewState.CheckPokemonIsFavorite(true))
            } ?: run {
                emit(PokemonDetailViewState.CheckPokemonIsFavorite(false))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}