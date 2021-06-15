package com.lozanov.viridum.api

import android.net.ConnectivityManager
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.lozanov.viridum.model.network.SketchFabModelResponse
import com.lozanov.viridum.shared.isConnected
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.io.IOException
import java.net.ConnectException
import java.util.concurrent.TimeUnit

interface ViridumAPI {

    @GET
    fun downloadModel(

    ): SketchFabModelResponse

    companion object {
        operator fun invoke(connectivityManager: ConnectivityManager): ViridumAPI {
            val requestInterceptor = Interceptor {
                try {
                    if(!connectivityManager.isConnected()) {
                        throw NoConnectivityException()
                    }

                    val headers = it.request().headers
                        .newBuilder()
                        .add("Accept", "*/*")
                        .add("Content-Type", if(it.request().method == "POST")
                            "application/x-www-form-urlencoded" else "application/json")
                        .add("Connection", "keep-alive")
                        .build()

                    val request = it.request()
                        .newBuilder()
                        .headers(headers)
                        .build()

                    return@Interceptor it.proceed(request)
                } catch(ex: ConnectException) {
                    throw NoConnectivityException()
                }
            }

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client: OkHttpClient = OkHttpClient()
                .newBuilder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(loggingInterceptor)
                .readTimeout(90, TimeUnit.SECONDS)
                .writeTimeout(90, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl("https://sketchfab.com")
                .client(client)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ViridumAPI::class.java)
        }
    }

    class NoConnectivityException : IOException()
}