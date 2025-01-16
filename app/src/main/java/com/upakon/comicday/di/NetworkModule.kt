package com.upakon.comicday.di

import android.content.Context
import com.google.gson.Gson
import com.upakon.comicday.data.service.DailyComicService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun providesComicService(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): DailyComicService =
        Retrofit.Builder()
            .baseUrl(DailyComicService.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(DailyComicService::class.java)

    @Provides
    fun providesOkHttp(
        cache: Cache,
        loggingInterceptor: HttpLoggingInterceptor
    ) : OkHttpClient =
        OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(loggingInterceptor)
            .build()

    @Provides
    fun providesGson(): Gson = Gson()

    @Provides
    fun providesCache(
        @ApplicationContext context: Context
    ) : Cache =
        Cache(File(context.cacheDir, "http-cache"),10L*1024L*1024L)

    @Provides
    fun providesLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    fun providesDispatcher(): CoroutineDispatcher = Dispatchers.IO

}