package itis.ru.justtalk.models.unsplash

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Urls(
    @SerializedName("raw")
    @Expose
    var raw: String? = null,
    @SerializedName("full")
    @Expose
    var full: String? = null,
    @SerializedName("regular")
    @Expose
    var regular: String? = null,
    @SerializedName("small")
    @Expose
    var small: String? = null,
    @SerializedName("thumb")
    @Expose
    var thumb: String? = null
)
