package itis.ru.justtalk.models

import com.firebase.ui.firestore.FirestoreRecyclerOptions

data class UidAndRecyclerOptions(val uid: String, val options: FirestoreRecyclerOptions<Message>)
