package br.com.henrique.pokedexmvvm.features.pokemonnavigation.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.henrique.pokedexmvvm.R
import br.com.henrique.pokedexmvvm.features.pokemonnavigation.adapter.TabAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_pokemon_navigation.*

class PokemonNavigationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_navigation)

        setupViews()
    }

    private fun setupViews() {
        viewPager.adapter = TabAdapter(this)
        TabLayoutMediator(tabLayout, viewPager){ tab, position ->
            when (position) {
                0 -> { tab.text = "Pokémons"}
                1 -> { tab.text = "Favoritos"}
            }
        }.attach()
    }
}