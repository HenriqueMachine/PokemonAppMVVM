package br.com.henrique.pokedexmvvm.application

import android.app.Application
import br.com.henrique.pokedexmvvm.data.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(level = Level.INFO)
            androidContext(this@MyApplication)
            modules(appModules)
        }
    }
}