package br.com.henrique.pokedexmvvm.features.pokemonlist.repository

import br.com.henrique.pokedexmvvm.data.client.PokedexAPI
import br.com.henrique.pokedexmvvm.data.model.CustomResponse
import br.com.henrique.pokedexmvvm.data.model.PokedexResponse
import br.com.henrique.pokedexmvvm.data.model.PokemonDetailResponse

class PokemonListRepository(private val api: PokedexAPI) : PokemonListService {

    override suspend fun getListPokemon(): CustomResponse<PokedexResponse> {
        return try {
            val result = api.getPokemons()
            CustomResponse.Success(result)
        } catch (e: Exception) {
            CustomResponse.Error(e)
        }
    }

    override suspend fun getDetailPokemon(url: String): CustomResponse<PokemonDetailResponse> {
        return try {
            val result = api.getPokemonDetail(url)
            CustomResponse.Success(result)
        } catch (e: Exception) {
            CustomResponse.Error(e)
        }
    }

    override suspend fun getNextPage(url: String): CustomResponse<PokedexResponse> {
        return try {
            val result = api.getPokemonsNextPage(url)
            CustomResponse.Success(result)
        } catch (e: Exception) {
            CustomResponse.Error(e)
        }
    }
}