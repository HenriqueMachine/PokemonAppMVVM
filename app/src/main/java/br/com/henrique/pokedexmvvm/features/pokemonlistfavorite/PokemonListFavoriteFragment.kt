package br.com.henrique.pokedexmvvm.features.pokemonlistfavorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.henrique.pokedexmvvm.R
import br.com.henrique.pokedexmvvm.data.db.model.PokemonEntity
import br.com.henrique.pokedexmvvm.features.pokemondetail.view.PokemonDetailActivity
import br.com.henrique.pokedexmvvm.features.pokemonlist.viewmodel.PokemonListViewModel
import br.com.henrique.pokedexmvvm.features.pokemonlistfavorite.adapter.PokemonFavoriteAdapter
import kotlinx.android.synthetic.main.fragment_pokemon_list_favorite.*
import org.koin.android.viewmodel.ext.android.viewModel


class PokemonListFavoriteFragment : Fragment() {

    companion object {
        const val IDENTIFIER_FAVORITE_POKEMON_LIST_FRAGMENT = "FavoritePokemonListFragment"
    }

    private val viewModel: PokemonListViewModel by viewModel()
    private var adapterListPokemon: PokemonFavoriteAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel.getPokemonsFromDatabase()
        setupObservers()

        return inflater.inflate(R.layout.fragment_pokemon_list_favorite, container, false)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPokemonsFromDatabase()
    }

    private fun setupObservers() {
        viewModel.pokemonFavoriteList.observe(requireActivity(), {
            adapterListPokemon = PokemonFavoriteAdapter(it)
            adapterListPokemon?.onClickFavoritePokemon = { detail ->
                openDetailActivity(detail)
            }
            recyclerViewFavoriteListPokemons.also { recyclerView ->
                recyclerView.adapter = adapterListPokemon
            }
        })
    }

    private fun openDetailActivity(data: PokemonEntity) {
        val intent = Intent(activity, PokemonDetailActivity::class.java)
        intent.putExtra(IDENTIFIER_FAVORITE_POKEMON_LIST_FRAGMENT, data)
        startActivity(intent)
    }
}