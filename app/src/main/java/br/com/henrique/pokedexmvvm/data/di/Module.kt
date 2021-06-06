package br.com.henrique.pokedexmvvm.data.di

import androidx.room.Room
import br.com.henrique.pokedexmvvm.features.pokemonlist.repository.PokemonListRepository
import br.com.henrique.pokedexmvvm.features.pokemonlist.repository.PokemonListService
import br.com.henrique.pokedexmvvm.features.pokemonlist.viewmodel.PokemonListViewModel
import br.com.henrique.pokedexmvvm.BuildConfig
import br.com.henrique.pokedexmvvm.data.client.PokedexAPI
import br.com.henrique.pokedexmvvm.data.client.RetrofitClient
import br.com.henrique.pokedexmvvm.data.db.database.PokemonDatabase
import br.com.henrique.pokedexmvvm.features.pokemondetail.viewmodel.PokemonDetailViewModel
import br.com.henrique.pokedexmvvm.features.pokemonlistfavorite.viewmodel.PokemonListFavoriteViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModules = module {
    single {
        RetrofitClient().createWebService<PokedexAPI>(
            okHttpClient = RetrofitClient().createHttpClient(),
            baseUrl = BuildConfig.BASE_POKEDEX_URL
        )
    }
    single {
        Room.databaseBuilder(
            get(),
            PokemonDatabase::class.java,
            "database.db")
            .build()
    }
    single { get<PokemonDatabase>().DAO() }
    factory<PokemonListService> { PokemonListRepository(api = get()) }
    viewModel { PokemonListViewModel(repository = get()) }
    viewModel { PokemonDetailViewModel(repository = get(), get()) }
    viewModel { PokemonListFavoriteViewModel(get()) }
}