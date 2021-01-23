package br.com.henrique.pokedexmvvm.data.db.dao

import androidx.room.*
import br.com.henrique.pokedexmvvm.data.db.model.PokemonEntity

@Dao
interface PokemonDao {

    @Query("SELECT * FROM pokemonTable")
    suspend fun getAllPokemons(): List<PokemonEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(pokemon: PokemonEntity)

    @Delete
    suspend fun deletePokemon(pokemon: PokemonEntity)

}