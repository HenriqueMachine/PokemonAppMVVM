package br.com.henrique.pokedexmvvm.features.pokemonlist.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import br.com.henrique.pokedexmvvm.R
import br.com.henrique.pokedexmvvm.data.model.ResultPokemonResponse
import br.com.henrique.pokedexmvvm.extensions.onStateChanged
import br.com.henrique.pokedexmvvm.features.pokemondetail.view.PokemonDetailActivity
import br.com.henrique.pokedexmvvm.features.pokemonlist.adapter.PokemonAdapter
import br.com.henrique.pokedexmvvm.features.pokemonlist.viewmodel.PokemonListViewModel
import kotlinx.android.synthetic.main.fragment_pokemon_list.*
import org.koin.android.viewmodel.ext.android.viewModel


class PokemonListFragment : Fragment() {

    companion object {
        const val IDENTIFIER_POKEMON_LIST_FRAGMENT = "PokemonListFragment"
    }

    private val viewModel: PokemonListViewModel by viewModel()
    private var adapterListPokemon: PokemonAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel.getList()
        setupObservers()

        return inflater.inflate(R.layout.fragment_pokemon_list, container, false)
    }

    private fun setupObservers() {
        viewModel.listPokemon.observe(requireActivity(), {
            adapterListPokemon = PokemonAdapter(ArrayList(it))
            adapterListPokemon?.onClickPokemon = { detail ->
                openDetailActivity(detail)
            }
            recyclerViewListPokemons.also { recyclerView ->
                recyclerView.adapter = adapterListPokemon
                recyclerView.onStateChanged { recyclerView, state ->
                    if (!recyclerView.canScrollVertically(1) && state == RecyclerView.SCROLL_STATE_IDLE) {
                        viewModel.getNextPage()
                    }
                }
            }
        })
        viewModel.listPokemonNextPage.observe(requireActivity(), {
            adapterListPokemon?.addElements(it)
        })
    }

    private fun openDetailActivity(data: ResultPokemonResponse) {
        val intent = Intent(activity, PokemonDetailActivity::class.java)
        intent.putExtra(IDENTIFIER_POKEMON_LIST_FRAGMENT, data)
        startActivity(intent)
    }
}