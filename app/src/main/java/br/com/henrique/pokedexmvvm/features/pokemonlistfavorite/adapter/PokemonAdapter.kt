package br.com.henrique.pokedexmvvm.features.pokemonlistfavorite.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.henrique.pokedexmvvm.R
import br.com.henrique.pokedexmvvm.data.db.model.PokemonEntity
import kotlinx.android.synthetic.main.pokemon_item_name.view.*

class PokemonFavoriteAdapter(private val list: List<PokemonEntity>) :
    RecyclerView.Adapter<PokemonFavoriteAdapter.PokemonFavoriteViewHolder>() {

    lateinit var onClickFavoritePokemon: (PokemonEntity) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonFavoriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pokemon_item_name, parent, false)

        return PokemonFavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonFavoriteViewHolder, position: Int) {
        holder.bindPokemon(list[position])
    }

    override fun getItemCount(): Int = list.size

    inner class PokemonFavoriteViewHolder(var view: View) : RecyclerView.ViewHolder(view) {

        fun bindPokemon(pokemonResponse: PokemonEntity) {
            with(view) {
                textViewPokemonName.text = pokemonResponse.name
                cardViewPokemon.setOnClickListener {
                    if (::onClickFavoritePokemon.isInitialized) {
                        onClickFavoritePokemon.invoke(pokemonResponse)
                    }
                }
            }
        }
    }
}