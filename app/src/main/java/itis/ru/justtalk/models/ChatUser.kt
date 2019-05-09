package itis.ru.justtalk.models

data class ChatUser(var uid: String, val name: String, val chats: Map<String, Boolean>) {
    constructor() : this("", "", mutableMapOf())
}