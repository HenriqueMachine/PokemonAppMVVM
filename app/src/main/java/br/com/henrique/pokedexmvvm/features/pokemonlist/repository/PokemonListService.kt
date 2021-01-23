package br.com.henrique.pokedexmvvm.features.pokemonlist.repository

import br.com.henrique.pokedexmvvm.data.model.CustomResponse
import br.com.henrique.pokedexmvvm.data.model.PokedexResponse
import br.com.henrique.pokedexmvvm.data.model.PokemonDetailResponse

interface PokemonListService{
    suspend fun getListPokemon() : CustomResponse<PokedexResponse>
    suspend fun getDetailPokemon(url: String) : CustomResponse<PokemonDetailResponse>
    suspend fun getNextPage(url: String) : CustomResponse<PokedexResponse>
}