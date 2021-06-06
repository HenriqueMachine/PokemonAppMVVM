package br.com.henrique.pokedexmvvm.application

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData

abstract class SingleStateFragment<S: Any>: Fragment() {

    abstract val viewModel: SingleStateViewModel<S>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe(viewModel) { logState(it); render(it) }
    }

    private fun logState(it: S) {
        Log.d("MVI", ">>> Rendering: ${it::class.java}")
    }

    override fun onStart() {
        super.onStart()
        configureBindings()
    }

    override fun onResume() {
        super.onResume()
        fetch()
    }

    private fun observe(viewModel: SingleStateViewModel<S>, block: (S) -> Unit) {
        viewModel.state.asLiveData().observe(viewLifecycleOwner, EventObserver(block))
    }

    abstract fun render(state: S)

    open fun fetch(){}
    open fun configureBindings(){}
}