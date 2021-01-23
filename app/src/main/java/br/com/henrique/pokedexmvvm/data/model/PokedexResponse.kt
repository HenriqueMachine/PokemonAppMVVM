package br.com.henrique.pokedexmvvm.data.model

import java.io.Serializable

data class PokedexResponse(
    val next: String,
    val results: List<ResultPokemonResponse>
)

data class ResultPokemonResponse(
    val name: String,
    val url: String
) : Serializable