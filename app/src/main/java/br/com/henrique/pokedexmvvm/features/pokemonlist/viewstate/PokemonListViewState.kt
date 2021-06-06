package br.com.henrique.pokedexmvvm.features.pokemonlist.viewstate

import br.com.henrique.pokedexmvvm.data.model.ResultPokemonResponse

sealed class PokemonListViewState {
    data class ShowPokemonList(val list : List<ResultPokemonResponse>) : PokemonListViewState()
    data class LoadNextPage(val listNextPage : List<ResultPokemonResponse>) : PokemonListViewState()
    data class ShowLoading(val isLoading : Boolean) : PokemonListViewState()
    object ShowError : PokemonListViewState()
}