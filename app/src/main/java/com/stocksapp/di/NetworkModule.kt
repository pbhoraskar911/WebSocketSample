package com.stocksapp.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.stocksapp.network.network.NetworkService
import com.stocksapp.network.network.Networking
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

/**
 * Created by Pranav Bhoraskar on 3/14/23
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideGsonBuilder(): Gson {
        return GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): NetworkService {
        return Networking.create()
    }
}