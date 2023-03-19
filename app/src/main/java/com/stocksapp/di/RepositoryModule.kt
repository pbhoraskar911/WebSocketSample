package com.stocksapp.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.stocksapp.data.repository.HoldingsRepository
import com.stocksapp.data.repository.HoldingsRepositoryImpl
import com.stocksapp.data.repository.StocksAppRepository
import com.stocksapp.data.repository.StocksAppRepositoryImpl
import com.stocksapp.network.network.NetworkService
import com.stocksapp.network.network.Networking
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

/**
 * Created by Pranav Bhoraskar on 3/4/23
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideStocksAppRepository(
        okHttpClient: OkHttpClient
    ): StocksAppRepository {
        return StocksAppRepositoryImpl(
            okHttpClient
        )
    }

    @Singleton
    @Provides
    fun provideHoldingsRepository(
        networkService: NetworkService
    ): HoldingsRepository {
        return HoldingsRepositoryImpl(
            networkService = networkService
        )
    }
}