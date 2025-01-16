package com.upakon.comicday.di

import com.upakon.comicday.data.repository.DailyComicRepository
import com.upakon.comicday.data.repository.DailyComicRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideComicRepository(
        dailyComicRepositoryImpl: DailyComicRepositoryImpl
    ): DailyComicRepository

}