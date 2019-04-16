package itis.ru.justtalk.utils

import java.net.URL

sealed class MyProfileState {
    class ShowUserInfo(val userName: String, val userAvatar: URL) : MyProfileState()
}
