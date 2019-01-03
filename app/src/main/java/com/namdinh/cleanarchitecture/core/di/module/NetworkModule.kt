package com.namdinh.cleanarchitecture.core.di.module

import android.app.Application
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.namdinh.cleanarchitecture.BuildConfig
import com.namdinh.cleanarchitecture.core.di.qualifier.ServerAddress
import com.namdinh.cleanarchitecture.data.remote.GithubService
import com.namdinh.cleanarchitecture.data.remote.helper.google.LiveDataCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DateFormat
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {

    companion object {
        private const val maxCacheSize = (10 * 1024 * 1024).toLong() // 10 MiB
        private const val timeout = 30L
    }

    @Provides
    @Singleton
    fun provideDefaultGsonBuilder(): GsonBuilder {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setDateFormat(DateFormat.FULL)
        return gsonBuilder
    }

    @Provides
    @Singleton
    fun provideLiveDataCallAdapterFactory(): LiveDataCallAdapterFactory {
        return LiveDataCallAdapterFactory()
    }

    @Provides
    @Singleton
    internal fun provideGson(gsonBuilder: GsonBuilder): Gson {
        return gsonBuilder.create()
    }

    @Provides
    @Singleton
    internal fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        return loggingInterceptor
    }

    @Provides
    @Singleton
    internal fun provideOkHttpCache(application: Application): Cache {
        return Cache(application.cacheDir, maxCacheSize)
    }

    @Provides
    @Named("cached")
    @Singleton
    internal fun provideCachedOkHttpClient(loggingInterceptor: HttpLoggingInterceptor, cache: Cache): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(loggingInterceptor)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Named("non_cached")
    @Singleton
    internal fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .readTimeout(timeout, TimeUnit.SECONDS)
            .connectTimeout(timeout, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    internal fun provideRetrofit(
        @ServerAddress serverAddress: String,
        gson: Gson,
        liveDataCallAdapterFactory: LiveDataCallAdapterFactory,
        @Named("non_cached") okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(liveDataCallAdapterFactory)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(serverAddress)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    internal fun provideIThinkApi(retrofit: Retrofit): GithubService {
        return retrofit.create(GithubService::class.java)
    }
}
