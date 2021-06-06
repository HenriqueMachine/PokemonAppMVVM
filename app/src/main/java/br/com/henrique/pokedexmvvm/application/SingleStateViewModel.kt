package br.com.henrique.pokedexmvvm.application

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

abstract class SingleStateViewModel<S: Any>: ViewModel() {
    private val _state = MutableSharedFlow<Event<S>>()
    val state get() = _state as SharedFlow<Event<S>>

    protected suspend fun emit(value: S) {
        logState(value); _state.emit(Event(value))
    }

    private fun logState(it: S) {
        Log.d("MVI", ">>> Emitting: ${it::class.java}")
    }
}