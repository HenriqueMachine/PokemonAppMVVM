package br.com.henrique.pokedexmvvm.features.pokemondetail.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.henrique.pokedexmvvm.R
import br.com.henrique.pokedexmvvm.application.SingleStateFragment
import br.com.henrique.pokedexmvvm.data.db.model.PokemonEntity
import br.com.henrique.pokedexmvvm.data.model.PokemonDetailResponse
import br.com.henrique.pokedexmvvm.data.model.ResultPokemonResponse
import br.com.henrique.pokedexmvvm.features.pokemondetail.viewmodel.PokemonDetailViewModel
import br.com.henrique.pokedexmvvm.features.pokemondetail.viewstate.PokemonDetailViewState
import br.com.henrique.pokedexmvvm.features.pokemondetail.viewstate.PokemonDetailViewState.*
import br.com.henrique.pokedexmvvm.features.pokemonlist.view.PokemonListFragment
import br.com.henrique.pokedexmvvm.features.pokemonlistfavorite.PokemonListFavoriteFragment
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_pokemon_detail.*
import org.koin.android.viewmodel.ext.android.viewModel

class DetailPokemonFragment : SingleStateFragment<PokemonDetailViewState>() {

    override val viewModel: PokemonDetailViewModel by viewModel()
    private var actualPokemon: PokemonEntity? = null
    private var favorite = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pokemon_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val detailPokemon = arguments?.getSerializable(
            PokemonListFragment.IDENTIFIER_POKEMON_LIST_FRAGMENT
        ) as ResultPokemonResponse?

        val favoriteDetailPokemon = arguments?.getSerializable(
            PokemonListFavoriteFragment.IDENTIFIER_FAVORITE_POKEMON_LIST_FRAGMENT
        ) as PokemonEntity?

        detailPokemon?.let { setupDetailPokemon(it) }
        favoriteDetailPokemon?.let { setupFavoriteDetailPokemon(it) }

    }

    private fun setupDetailPokemon(detailPokemon: ResultPokemonResponse) {
        viewModel.getDetail(detailPokemon.url)
        viewModel.getPokemonsFromDatabase()
        setupListeners()
    }

    private fun setupFavoriteDetailPokemon(detailPokemon: PokemonEntity) {
        getDetailFavoritePokemon(detailPokemon)
        setupListeners()
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
            activity?.onBackPressed()
        }
        imageViewFavoritePokemon.setOnClickListener {
            actualPokemon?.let { pokemon ->
                if (favorite) removeFavoritePokemon(it, pokemon)
                else addFavoritePokemon(it, pokemon)
            }
        }
    }

    override fun render(state: PokemonDetailViewState) {
        when (state) {
            is ShowDetailPokemon -> {
                state.detail.also {
                    setImagePokemon(it.sprites.front_default)
                    textViewPokemonIdDetail.text = "#${it.id}"
                    textViewPokemonNameDetail.text = it.name.toUpperCase()
                    textViewPokemonTypeDetail.text = it.types.first().type.name.toUpperCase()
                    viewModel.checkPokemonIsFavorite(it.id)
                    actualPokemon = it.mapToPokemonEntity()
                }
            }
            is GetFavoritePokemons -> {
                viewModel.checkPokemonIsFavorite(actualPokemon?.id ?: 0)
            }
            is CheckPokemonIsFavorite -> {
                setFavoriteState(state.isFavorite)
            }
        }
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
}