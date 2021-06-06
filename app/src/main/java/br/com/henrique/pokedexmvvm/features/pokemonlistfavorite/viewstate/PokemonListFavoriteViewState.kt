package br.com.henrique.pokedexmvvm.features.pokemonlistfavorite.viewstate

import br.com.henrique.pokedexmvvm.data.db.model.PokemonEntity

sealed class PokemonListFavoriteViewState {
    data class ShowListFavorite(val list: List<PokemonEntity>) : PokemonListFavoriteViewState()
}
