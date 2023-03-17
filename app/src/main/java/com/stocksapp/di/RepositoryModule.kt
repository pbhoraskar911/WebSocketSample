package com.stocksapp.di

import com.stocksapp.data.repository.StocksAppRepository
import com.stocksapp.data.repository.StocksAppRepositoryImpl
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
}