package br.com.henrique.pokedexmvvm.data.client

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient {

    fun createHttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.readTimeout(5 * 60, TimeUnit.SECONDS)
        return client.addInterceptor {
            val original = it.request()
            val requestBuilder = original.newBuilder()
            requestBuilder.header("Content-Type", "application/json")
            val request = requestBuilder.method(original.method(), original.body()).build()
            return@addInterceptor it.proceed(request)
        }.build()
    }

    inline fun <reified T> createWebService(
            okHttpClient: OkHttpClient, baseUrl: String
    ): T {
        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(okHttpClient)
                .build()
        return retrofit.create(T::class.java)
    }
}