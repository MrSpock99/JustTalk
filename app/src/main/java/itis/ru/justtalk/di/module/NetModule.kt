package itis.ru.justtalk.di.module

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import itis.ru.justtalk.api.UnsplashImageApi
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetModule {
    @Provides
    fun provideBaseUrl() = "https://api.unsplash.com/"

    @Provides
    fun provideConverterFactory(): GsonConverterFactory =
        GsonConverterFactory.create(GsonBuilder().setLenient().create())

    @Provides
    fun provideCallAdapterFactory(): RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()

    @Provides
    fun provideRetrofit(
        baseUrl: String,
        converterFactory: GsonConverterFactory,
        callAdapterFactory: RxJava2CallAdapterFactory
    ): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(converterFactory)
        .addCallAdapterFactory(callAdapterFactory)
        .build()

    @Provides
    fun provideUnsplashImageApi(retrofit: Retrofit): UnsplashImageApi =
        retrofit.create(UnsplashImageApi::class.java)

}
