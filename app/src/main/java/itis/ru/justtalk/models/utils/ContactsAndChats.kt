package itis.ru.justtalk.models.utils

import itis.ru.justtalk.models.user.RemoteChatUser

data class ContactsAndChats(
    val contactsList: MutableList<RemoteChatUser>,
    val chatsList: List<String>
)
