package br.com.henrique.pokedexmvvm.features.pokemonlistfavorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import br.com.henrique.pokedexmvvm.R
import br.com.henrique.pokedexmvvm.application.SingleStateFragment
import br.com.henrique.pokedexmvvm.data.db.model.PokemonEntity
import br.com.henrique.pokedexmvvm.features.pokemonlistfavorite.adapter.PokemonFavoriteAdapter
import br.com.henrique.pokedexmvvm.features.pokemonlistfavorite.viewmodel.PokemonListFavoriteViewModel
import br.com.henrique.pokedexmvvm.features.pokemonlistfavorite.viewstate.PokemonListFavoriteViewState
import kotlinx.android.synthetic.main.fragment_pokemon_list_favorite.*
import org.koin.android.viewmodel.ext.android.viewModel


class PokemonListFavoriteFragment : SingleStateFragment<PokemonListFavoriteViewState>() {

    companion object {
        const val IDENTIFIER_FAVORITE_POKEMON_LIST_FRAGMENT = "FavoritePokemonListFragment"
    }

    override val viewModel: PokemonListFavoriteViewModel by viewModel()
    private var adapterListPokemon: PokemonFavoriteAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.getPokemonsFromDatabase()

        return inflater.inflate(R.layout.fragment_pokemon_list_favorite, container, false)
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPokemonsFromDatabase()
    }

    override fun render(state: PokemonListFavoriteViewState) {
        when (state) {
            is PokemonListFavoriteViewState.ShowListFavorite -> {
                adapterListPokemon = PokemonFavoriteAdapter(state.list)
                adapterListPokemon?.onClickFavoritePokemon = { detail ->
                    openDetailActivity(detail)
                }
                recyclerViewFavoriteListPokemons.also { recyclerView ->
                    recyclerView.adapter = adapterListPokemon
                }
            }
        }
    }

    private fun openDetailActivity(data: PokemonEntity) {
        findNavController().navigate(
            R.id.action_listPokemonFavorite_to_pokemonDetail,
            bundleOf(Pair(IDENTIFIER_FAVORITE_POKEMON_LIST_FRAGMENT, data))
        )
    }

}