package com.example.data.remote

import android.content.Context
import com.example.R
import com.google.firebase.firestore.FirebaseFirestore

object FirestoreProvider {
    fun get(context: Context): FirebaseFirestore {
        val dbId = context.getString(R.string.firestore_database_id)
        return FirebaseFirestore.getInstance(dbId)
    }
}
