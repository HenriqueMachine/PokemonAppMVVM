package br.com.henrique.pokedexmvvm.application

import androidx.lifecycle.Observer

open class Event<out T>(private val _content: T) {

    var hasBeenHandled = false
        private set

    val content get() = _content

    fun getIfNotHandled(): T? {
        return if(hasBeenHandled) null
        else _content.also { hasBeenHandled = true }
    }

}

class EventObserver<T>(private val block: (T) -> Unit): Observer<Event<T>>{

    override fun onChanged(event: Event<T>?) {
        event?.getIfNotHandled()?.let { block(it) }
    }
}