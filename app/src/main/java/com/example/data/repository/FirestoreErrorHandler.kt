package com.example.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestoreException
import org.json.JSONObject

enum class OperationType {
    CREATE, UPDATE, DELETE, LIST, GET, WRITE
}

fun handleFirestoreError(
    error: Throwable,
    operationType: OperationType,
    path: String,
    auth: FirebaseAuth
): FirebaseFirestoreException {
    val user = auth.currentUser
    val errorInfo = JSONObject().apply {
        put("error", error.message ?: "Unknown Firestore error")
        put("operationType", operationType.name.lowercase())
        put("path", path)
        put("authInfo", JSONObject().apply {
            put("userId", user?.uid)
            put("email", user?.email)
            put("emailVerified", user?.isEmailVerified)
            put("isAnonymous", user?.isAnonymous)
            put("providerInfo", user?.providerData?.map {
                "${it.providerId}:${it.email ?: ""}"
            })
        })
    }
    Log.e("FirestoreError", errorInfo.toString(), error)
    return if (error is FirebaseFirestoreException) error
    else FirebaseFirestoreException(
        error.message ?: "Firestore operation failed",
        FirebaseFirestoreException.Code.ABORTED,
        error
    )
}
