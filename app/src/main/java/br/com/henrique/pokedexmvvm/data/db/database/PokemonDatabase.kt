package br.com.henrique.pokedexmvvm.data.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.henrique.pokedexmvvm.data.db.dao.PokemonDao
import br.com.henrique.pokedexmvvm.data.db.model.PokemonEntity

@Database(entities = [PokemonEntity::class], version = 1, exportSchema = false)
abstract class PokemonDatabase : RoomDatabase() {

    abstract fun DAO(): PokemonDao

}