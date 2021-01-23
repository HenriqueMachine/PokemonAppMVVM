package br.com.henrique.pokedexmvvm.features.pokemondetail.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.com.henrique.pokedexmvvm.R
import br.com.henrique.pokedexmvvm.data.db.model.PokemonEntity
import br.com.henrique.pokedexmvvm.data.model.PokemonDetailResponse
import br.com.henrique.pokedexmvvm.data.model.ResultPokemonResponse
import br.com.henrique.pokedexmvvm.features.pokemonlist.view.PokemonListFragment
import br.com.henrique.pokedexmvvm.features.pokemonlist.viewmodel.PokemonListViewModel
import br.com.henrique.pokedexmvvm.features.pokemonlistfavorite.PokemonListFavoriteFragment
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_pokemon_detail.*
import org.koin.android.viewmodel.ext.android.viewModel

class PokemonDetailActivity : AppCompatActivity() {

    private val viewModel: PokemonListViewModel by viewModel()
    private var actualPokemon: PokemonEntity? = null
    private var favorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_detail)

        val detailPokemon = intent.extras?.getSerializable(
            PokemonListFragment.IDENTIFIER_POKEMON_LIST_FRAGMENT
        ) as ResultPokemonResponse?

        val favoriteDetailPokemon = intent.extras?.getSerializable(
            PokemonListFavoriteFragment.IDENTIFIER_FAVORITE_POKEMON_LIST_FRAGMENT
        ) as PokemonEntity?

        detailPokemon?.let { setupDetailPokemon(it) }
        favoriteDetailPokemon?.let { setupFavoriteDetailPokemon(it) }

    }

    private fun setupDetailPokemon(detailPokemon: ResultPokemonResponse) {
        viewModel.getDetail(detailPokemon.url)
        viewModel.getPokemonsFromDatabase()
        setupObservers()
        setupListeners()
    }

    private fun setupFavoriteDetailPokemon(detailPokemon: PokemonEntity) {
        getDetailFavoritePokemon(detailPokemon)
        setupListeners()
        setupDetailObservers()
    }

    private fun getDetailFavoritePokemon(detailPokemon: PokemonEntity) {
        actualPokemon = detailPokemon
        viewModel.getPokemonsFromDatabase()
        textViewPokemonIdDetail.text = "#${detailPokemon.id}"
        textViewPokemonNameDetail.text = detailPokemon.name.toUpperCase()
        textViewPokemonTypeDetail.text = detailPokemon.type.toUpperCase()
        setImagePokemon(detailPokemon.photoUrl)
    }

    private fun setupListeners() {
        imageViewArrowBack.setOnClickListener {
            onBackPressed()
        }
        imageViewFavoritePokemon.setOnClickListener {
            actualPokemon?.let { pokemon ->
                if (favorite) removeFavoritePokemon(it, pokemon)
                else addFavoritePokemon(it, pokemon)
            }
        }
    }

    private fun setupObservers() {
        viewModel.pokemonDetail.observe(this, {
            setImagePokemon(it.sprites.front_default)
            textViewPokemonIdDetail.text = "#${it.id}"
            textViewPokemonNameDetail.text = it.name.toUpperCase()
            textViewPokemonTypeDetail.text = it.types.first().type.name.toUpperCase()
            viewModel.checkPokemonIsFavorite(it.id)
            actualPokemon = it.mapToPokemonEntity()

        })
        viewModel.pokemonIsFavorite.observe(this, {
            setFavoriteState(it)
        })
    }

    private fun setupDetailObservers() {
        viewModel.pokemonIsFavorite.observe(this, {
            setFavoriteState(it)
        })
        viewModel.pokemonFavoriteList.observe(this, {
            viewModel.checkPokemonIsFavorite(actualPokemon?.id ?: 0)
        })
    }

    private fun setFavoriteState(it: Boolean) {
        favorite = it
        if (it) imageViewFavoritePokemon.setImageResource(R.drawable.ic_favorite_full)
        else imageViewFavoritePokemon.setImageResource(R.drawable.ic_favorite_empty)
    }

    private fun addFavoritePokemon(
        view: View,
        pokemon: PokemonEntity
    ) {
        favorite = true
        imageViewFavoritePokemon.setImageResource(R.drawable.ic_favorite_full)
        Snackbar.make(view, "Pokémon adicionado nos favoritos", Snackbar.LENGTH_SHORT).show()
        viewModel.insertPokemon(pokemon)
    }

    private fun removeFavoritePokemon(
        it: View,
        pokemon: PokemonEntity
    ) {
        favorite = false
        imageViewFavoritePokemon.setImageResource(R.drawable.ic_favorite_empty)
        Snackbar.make(it, "Pokémon removido dos favoritos", Snackbar.LENGTH_SHORT).show()
        viewModel.removePokemon(pokemon)
    }

    private fun setImagePokemon(url: String) {
        Glide
            .with(this)
            .asBitmap()
            .load(url)
            .centerCrop()
            .into(imageViewPokemonPhoto)
    }

    private fun PokemonDetailResponse.mapToPokemonEntity(): PokemonEntity {
        return PokemonEntity(
            id = this.id,
            name = this.name.toUpperCase(),
            type = this.types.first().type.name.toUpperCase(),
            photoUrl = this.sprites.front_default
        )
    }

    override fun onBackPressed() {
        finish()
    }
}