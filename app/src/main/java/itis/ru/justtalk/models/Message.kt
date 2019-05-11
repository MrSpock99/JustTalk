package itis.ru.justtalk.models

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Message(
    val messageText: String = "",
    val fromUid: String = "",
    @ServerTimestamp
    val sentAt: Date? = null
)
