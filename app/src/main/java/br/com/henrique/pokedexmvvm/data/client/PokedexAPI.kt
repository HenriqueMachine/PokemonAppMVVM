package br.com.henrique.pokedexmvvm.data.client

import br.com.henrique.pokedexmvvm.data.URLs
import br.com.henrique.pokedexmvvm.data.model.PokedexResponse
import br.com.henrique.pokedexmvvm.data.model.PokemonDetailResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

const val DEFAULT_LIMIT = 100

interface PokedexAPI {

    @GET(URLs.GET_POKEMON_LIST)
    suspend fun getPokemons(
        @Query("limit") limit: Int = DEFAULT_LIMIT,
        @Query("offset") offset: Int = 0
    ): PokedexResponse

    @GET
    suspend fun getPokemonDetail(
        @Url url: String
    ): PokemonDetailResponse

    @GET
    suspend fun getPokemonsNextPage(
        @Url url: String
    ): PokedexResponse

}