package com.example.calculator.business.dependencies

import android.content.Context
import com.example.calculator.BuildConfig
import com.example.calculator.business.network.CalculatorClient
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    internal fun provideOkHttpClient(context: Context): OkHttpClient {
        val builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.HEADERS
            builder.addInterceptor(interceptor)
            builder.addNetworkInterceptor(StethoInterceptor())
            builder.addNetworkInterceptor(ChuckInterceptor(context))
        }
        return builder.build()
    }

    @Provides
    @Singleton
    internal fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    internal fun provideCalculatorClient(okHttpClient: OkHttpClient, gsonConverterFactory: GsonConverterFactory):CalculatorClient {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            .addConverterFactory(gsonConverterFactory)
            .build().create(CalculatorClient::class.java)
    }

    companion object {
        private const val BASE_URL = "https://127.0.0.1/"
    }
}