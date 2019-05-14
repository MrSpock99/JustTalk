package itis.ru.justtalk.models.utils

import com.firebase.ui.firestore.FirestoreRecyclerOptions
import itis.ru.justtalk.models.Message

data class UidAndRecyclerOptions(val uid: String, val options: FirestoreRecyclerOptions<Message>)
