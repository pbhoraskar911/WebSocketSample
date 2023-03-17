package com.stocksapp.di

import com.stocksapp.data.repository.StocksAppRepository
import com.stocksapp.domain.StocksDataUseCase
import com.stocksapp.domain.StocksDataUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Pranav Bhoraskar on 3/15/23
 */
@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Singleton
    @Provides
    fun provideStocksDataUseCase(
        repository: StocksAppRepository
    ): StocksDataUseCase {
        return StocksDataUseCaseImpl(repository)
    }
}