package br.com.henrique.pokedexmvvm.features.listselector

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import br.com.henrique.pokedexmvvm.R
import kotlinx.android.synthetic.main.fragment_list_selector.*

class ListSelectorFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_selector, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        materialButtonPokemonList.setOnClickListener {
            findNavController().navigate(R.id.action_availableLists_to_listPokemon)
        }

        materialButtonPokemonListFavorite.setOnClickListener {
            findNavController().navigate(R.id.action_availableLists_to_listPokemonFavorite)
        }

    }

}