package itis.ru.justtalk.models

data class AppUser(
    val name: String,
    val age: Int,
    val gender: String,
    val avatar_url: String
) {
    constructor() : this("", 0, "", "")
}