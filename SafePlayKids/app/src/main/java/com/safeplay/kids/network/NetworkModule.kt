package com.safeplay.kids.di

import android.content.Context
import com.safeplay.kids.network.AndroidHeaderInterceptor
import com.safeplay.kids.network.YouTubeRepository
import com.safeplay.kids.pojo.YouTubeApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE = "https://www.googleapis.com/youtube/v3/"

    @Provides @Singleton
    fun okHttp(@ApplicationContext ctx: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                AndroidHeaderInterceptor(
                    ctx         // extension shown below
                )
            )
            .build()
    }

    @Provides @Singleton
    fun retrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides @Singleton fun api(retro: Retrofit): YouTubeApiService = retro.create(YouTubeApiService::class.java)
    @Provides @Singleton fun repo(api: YouTubeApiService) = YouTubeRepository(api)
}
