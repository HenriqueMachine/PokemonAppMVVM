package br.com.henrique.pokedexmvvm.features.pokemonlist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.henrique.pokedexmvvm.R
import br.com.henrique.pokedexmvvm.data.model.ResultPokemonResponse
import kotlinx.android.synthetic.main.pokemon_item_name.view.*

class PokemonAdapter(private val list: ArrayList<ResultPokemonResponse>) :
    RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    lateinit var onClickPokemon: (ResultPokemonResponse) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pokemon_item_name, parent, false)

        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.bindPokemon(list[position])
    }

    override fun getItemCount(): Int = list.size

    fun addElements(list: List<ResultPokemonResponse>) {
        this.list.addAll(list)
        this.notifyDataSetChanged()
    }

    inner class PokemonViewHolder(var view: View) : RecyclerView.ViewHolder(view) {

        fun bindPokemon(pokemonResponse: ResultPokemonResponse) {
            with(view) {
                textViewPokemonName.text = pokemonResponse.name.toUpperCase()
                cardViewPokemon.setOnClickListener {
                    if (::onClickPokemon.isInitialized) {
                        onClickPokemon.invoke(pokemonResponse)
                    }
                }
            }
        }
    }
}