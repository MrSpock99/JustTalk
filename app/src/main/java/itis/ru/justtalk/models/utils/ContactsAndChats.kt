package itis.ru.justtalk.models.utils

import itis.ru.justtalk.models.user.ChatUser

data class ContactsAndChats(val contactsList: List<ChatUser>, val chatsList: List<String>)
