package com.example.auth

import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
import com.example.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class GoogleAuthHelper(private val activityContext: Context) {

    private val credentialManager = CredentialManager.create(activityContext)

    private fun webClientId(): String =
        activityContext.getString(R.string.default_web_client_id)

    suspend fun attemptAutoSignIn(): Result<FirebaseUser?> = runCatching {
        val current = Firebase.auth.currentUser
        if (current != null) return@runCatching current

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(webClientId())
            .setAutoSelectEnabled(true)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        try {
            val result = credentialManager.getCredential(
                request = request,
                context = activityContext
            )
            handleCredential(result.credential)
        } catch (e: GetCredentialCancellationException) {
            Log.w("Auth", "Auto sign-in cancelled by user", e)
            null
        } catch (e: NoCredentialException) {
            Log.i("Auth", "No saved Google credentials for auto sign-in", e)
            null
        }
    }

    suspend fun signInWithGoogleButton(): Result<FirebaseUser> = runCatching {
        val signInOption = GetSignInWithGoogleOption.Builder(webClientId()).build()
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(signInOption)
            .build()

        val result = try {
            credentialManager.getCredential(
                request = request,
                context = activityContext
            )
        } catch (e: GetCredentialCancellationException) {
            Log.w("Auth", "Google sign-in cancelled", e)
            throw e
        }

        handleCredential(result.credential)
            ?: throw IllegalStateException("Google Sign-In did not return a valid Firebase user.")
    }

    private suspend fun handleCredential(credential: androidx.credentials.Credential): FirebaseUser? {
        if (credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            val firebaseCredential = GoogleAuthProvider.getCredential(
                googleIdTokenCredential.idToken,
                null
            )
            val authResult = Firebase.auth.signInWithCredential(firebaseCredential).await()
            return authResult.user
        }
        throw IllegalStateException("Unexpected credential type: ${credential.type}")
    }

    suspend fun signOut() {
        withContext(NonCancellable) {
            runCatching {
                credentialManager.clearCredentialState(ClearCredentialStateRequest())
            }.onFailure {
                Log.w("Auth", "clearCredentialState failed", it)
            }
            Firebase.auth.signOut()
        }
    }
}
