package com.namdinh.cleanarchitecture.core.di.module

import android.app.Application
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.namdinh.cleanarchitecture.BuildConfig
import com.namdinh.cleanarchitecture.core.di.scope.PerApplication
import com.namdinh.cleanarchitecture.data.network.GithubService
import com.namdinh.cleanarchitecture.data.network.helper.google.LiveDataCallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DateFormat
import java.util.concurrent.TimeUnit
import javax.inject.Named

@Module
class NetworkModule {

    @Provides
    @PerApplication
    fun provideDefaultGsonBuilder(): GsonBuilder {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.setDateFormat(DateFormat.FULL)
        return gsonBuilder
    }

    @Provides
    @PerApplication
    fun provideLiveDataCallAdapterFactory(): LiveDataCallAdapterFactory {
        return LiveDataCallAdapterFactory()
    }

    @Provides
    @PerApplication
    internal fun provideGson(gsonBuilder: GsonBuilder): Gson {
        return gsonBuilder.create()
    }

    @Provides
    @PerApplication
    internal fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        return loggingInterceptor
    }

    @Provides
    @PerApplication
    internal fun provideOkHttpCache(application: Application): Cache {
        return Cache(application.cacheDir, (10 * 1024 * 1024).toLong())  // 10 MiB
    }

    @Provides
    @Named("cached")
    @PerApplication
    internal fun provideCachedOkHttpClient(loggingInterceptor: HttpLoggingInterceptor, cache: Cache): OkHttpClient {
        return OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(loggingInterceptor)
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build()
    }

    @Provides
    @Named("non_cached")
    @PerApplication
    internal fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build()
    }

    @Provides
    @PerApplication
    internal fun provideRetrofit(@Named("server_address") serverAddress: String,
                                 gson: Gson, liveDataCallAdapterFactory: LiveDataCallAdapterFactory,
                                 @Named("non_cached") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(liveDataCallAdapterFactory)
                .baseUrl(serverAddress)
                .client(okHttpClient)
                .build()
    }

    @Provides
    @PerApplication
    internal fun provideIThinkApi(retrofit: Retrofit): GithubService {
        return retrofit.create(GithubService::class.java)
    }
}
