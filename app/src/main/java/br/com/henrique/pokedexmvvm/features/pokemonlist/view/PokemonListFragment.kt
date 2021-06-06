package br.com.henrique.pokedexmvvm.features.pokemonlist.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import br.com.henrique.pokedexmvvm.R
import br.com.henrique.pokedexmvvm.application.SingleStateFragment
import br.com.henrique.pokedexmvvm.data.model.ResultPokemonResponse
import br.com.henrique.pokedexmvvm.extensions.onStateChanged
import br.com.henrique.pokedexmvvm.features.pokemonlist.adapter.PokemonAdapter
import br.com.henrique.pokedexmvvm.features.pokemonlist.viewmodel.PokemonListViewModel
import br.com.henrique.pokedexmvvm.features.pokemonlist.viewstate.PokemonListViewState
import br.com.henrique.pokedexmvvm.features.pokemonlist.viewstate.PokemonListViewState.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_pokemon_list.*
import org.koin.android.viewmodel.ext.android.viewModel


class PokemonListFragment : SingleStateFragment<PokemonListViewState>() {

    companion object {
        const val IDENTIFIER_POKEMON_LIST_FRAGMENT = "PokemonListFragment"
    }

    override val viewModel: PokemonListViewModel by viewModel()
    private var adapterListPokemon: PokemonAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel.getList()

        return inflater.inflate(R.layout.fragment_pokemon_list, container, false)
    }

    override fun render(state: PokemonListViewState) {
        when (state) {
            is ShowPokemonList -> {
                adapterListPokemon = PokemonAdapter(ArrayList(state.list))
                adapterListPokemon?.onClickPokemon = { detail ->
                    openDetailActivity(detail)
                }
                recyclerViewListPokemons.also { recyclerView ->
                    recyclerView.adapter = adapterListPokemon
                    recyclerView.onStateChanged { recyclerView2, state ->
                        if (!recyclerView2.canScrollVertically(1) && state == RecyclerView.SCROLL_STATE_IDLE) {
                            viewModel.getNextPage()
                        }
                    }
                }
            }
            is LoadNextPage -> {
                adapterListPokemon?.addElements(state.listNextPage)
            }
            is ShowLoading -> {
                if (state.isLoading) progressListPokemon.visibility = View.VISIBLE
                else progressListPokemon.visibility = View.GONE
            }
            is ShowError -> {
                view?.let { Snackbar.make(it, "Erro ao carregar lista", Snackbar.LENGTH_SHORT).show() }
            }
        }
    }

    private fun openDetailActivity(data: ResultPokemonResponse) {
        findNavController().navigate(
            R.id.action_listPokemon_to_pokemonDetail,
            bundleOf(Pair(IDENTIFIER_POKEMON_LIST_FRAGMENT, data))
        )
    }
}