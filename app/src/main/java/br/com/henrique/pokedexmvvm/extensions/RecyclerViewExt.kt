package br.com.henrique.pokedexmvvm.extensions

import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.onStateChanged(stateChanged: (RecyclerView, Int) -> Unit) {
    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            stateChanged.invoke(recyclerView, newState)
        }
    })
}