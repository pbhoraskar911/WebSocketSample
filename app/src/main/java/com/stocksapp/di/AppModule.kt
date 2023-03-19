package com.stocksapp.di

import com.computations.PLLogicInterface
import com.computations.PLLogicInterfaceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Pranav Bhoraskar on 3/19/23
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideComputations(): PLLogicInterface {
        return PLLogicInterfaceImpl()
    }

}