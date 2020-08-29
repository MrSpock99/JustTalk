package itis.ru.justtalk.api

import io.reactivex.Observable
import itis.ru.justtalk.models.unsplash.UnsplashImage
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface UnsplashImageApi {
    @GET("search/photos?per_page=10")
    fun getPhotoByKeyword(
        @Header("Accept-Version") version: String = "v1",
        @Header("Authorization") auth: String = "Client-ID 23dae167e5da4d40bf49bd869a91840b31adcec9b1011580874a24dd6a9ece6a",
        @Query("query") keyword: String
    ): Observable<UnsplashImage>
}
