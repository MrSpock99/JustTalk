package itis.ru.justtalk.models.unsplash

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("urls")
    @Expose
    var urls: Urls? = null
)
