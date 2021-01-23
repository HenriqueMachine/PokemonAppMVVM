package br.com.henrique.pokedexmvvm.features.pokemonlist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.henrique.pokedexmvvm.BuildConfig
import br.com.henrique.pokedexmvvm.data.db.database.PokemonDatabase
import br.com.henrique.pokedexmvvm.data.db.model.PokemonEntity
import br.com.henrique.pokedexmvvm.data.model.CustomResponse
import br.com.henrique.pokedexmvvm.data.model.PokemonDetailResponse
import br.com.henrique.pokedexmvvm.data.model.ResultPokemonResponse
import br.com.henrique.pokedexmvvm.features.pokemonlist.repository.PokemonListService
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class PokemonListViewModel(
    private val repository: PokemonListService,
    private val database: PokemonDatabase
) : ViewModel(),
    CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + job
    private var pathNextList = ""
    private var favoriteList: List<PokemonEntity>? = null

    private val listPokemontState = MutableLiveData<List<ResultPokemonResponse>>()
    val listPokemon: LiveData<List<ResultPokemonResponse>> get() = listPokemontState

    private val listPokemonNextPageState = MutableLiveData<List<ResultPokemonResponse>>()
    val listPokemonNextPage: LiveData<List<ResultPokemonResponse>> get() = listPokemonNextPageState

    private val pokemonDetailState = MutableLiveData<PokemonDetailResponse>()
    val pokemonDetail: LiveData<PokemonDetailResponse> get() = pokemonDetailState

    private val pokemonListFavoriteState = MutableLiveData<List<PokemonEntity>>()
    val pokemonFavoriteList: LiveData<List<PokemonEntity>> get() = pokemonListFavoriteState

    private val pokemonIsFavoriteState = MutableLiveData<Boolean>()
    val pokemonIsFavorite: LiveData<Boolean> get() = pokemonIsFavoriteState

    fun getList() {
        launch {
            val result = withContext(Dispatchers.IO) {
                repository.getListPokemon()
            }

            when (result) {
                is CustomResponse.Success -> {
                    listPokemontState.value = result.data.results
                    pathNextList = result.data.next.removePrefix(BuildConfig.BASE_POKEDEX_URL)
                }
                is CustomResponse.Error -> {
                }
            }
        }
    }

    fun getDetail(url: String) {
        launch {
            val result = withContext(Dispatchers.IO) {
                val detail = url.removePrefix(BuildConfig.BASE_POKEDEX_URL)
                repository.getDetailPokemon(detail)
            }

            when (result) {
                is CustomResponse.Success -> {
                    pokemonDetailState.value = result.data
                }
                is CustomResponse.Error -> {
                }
            }
        }
    }

    fun getNextPage() {
        launch {
            val result = withContext(Dispatchers.IO) {
                repository.getNextPage(pathNextList)
            }

            when (result) {
                is CustomResponse.Success -> {
                    listPokemonNextPageState.value = result.data.results
                    pathNextList = result.data.next.removePrefix(BuildConfig.BASE_POKEDEX_URL)
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
                pokemonListFavoriteState.value = it
            }
        }
    }

    fun checkPokemonIsFavorite(pokemonId: Int) {
        launch {
            val favorite = favoriteList?.firstOrNull { pokemonId == it.id }
            favorite?.let {
                pokemonIsFavoriteState.value = true
            } ?: run {
                pokemonIsFavoriteState.value = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}