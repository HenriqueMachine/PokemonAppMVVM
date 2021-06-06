package br.com.henrique.pokedexmvvm.features.pokemondetail.viewstate

import br.com.henrique.pokedexmvvm.data.db.model.PokemonEntity
import br.com.henrique.pokedexmvvm.data.model.PokemonDetailResponse

sealed class PokemonDetailViewState {
    data class ShowDetailPokemon(val detail: PokemonDetailResponse) : PokemonDetailViewState()
    data class GetFavoritePokemons(val list: List<PokemonEntity>) : PokemonDetailViewState()
    data class CheckPokemonIsFavorite(val isFavorite: Boolean) : PokemonDetailViewState()
}