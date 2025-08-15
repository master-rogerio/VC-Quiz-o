package com.example.vcquizo.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.vcquizo.domain.model.User
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.PersistentCacheSettings
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

// Extensão para DataStore
val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserRepository(private val context: Context) {
    private val firestore = Firebase.firestore
    private val usersCollection = firestore.collection("users")

    // Chaves para DataStore
    private object PrefsKeys {
        val UID = stringPreferencesKey("uid")
        val EMAIL = stringPreferencesKey("email")
        val NAME = stringPreferencesKey("name")
    }

//    init {
//        // HABILITA A PERSISTÊNCIA OFFLINE
//        val settings = FirebaseFirestoreSettings.Builder()
//            .setLocalCacheSettings(
//                PersistentCacheSettings.newBuilder()
//                    .setSizeBytes(10L * 1024 * 1024) // Exemplo: 10 MB
//                    .build()
//            )
//            .build()
//
//        FirebaseFirestore.getInstance().firestoreSettings = settings
//    }

    /**
     * Procura um usuário no DataStore local pelo email.
     * Usado para a lógica de login offline.
     */
    suspend fun getUserByEmailLocal(email: String): User? {
        val prefs = context.dataStore.data.firstOrNull()
        return if (prefs?.get(PrefsKeys.EMAIL)?.equals(email, ignoreCase = true) == true) {
            User(
                uid = prefs[PrefsKeys.UID] ?: "",
                email = prefs[PrefsKeys.EMAIL] ?: "",
                name = prefs[PrefsKeys.NAME]
            )
        } else {
            null
        }
    }


    /**
     * Salva perfil localmente (DataStore)
     */
    suspend fun saveUserLocal(user: User) {
        context.dataStore.edit { prefs ->
            prefs[PrefsKeys.UID] = user.uid
            prefs[PrefsKeys.EMAIL] = user.email
            user.name?.let { prefs[PrefsKeys.NAME] = it }
        }
    }

    /**
     * Obtém perfil local (Flow observável)
     */
    val userLocal: Flow<User?> = context.dataStore.data.map { prefs ->
        prefs[PrefsKeys.UID]?.let { uid ->
            User(
                uid = uid,
                email = prefs[PrefsKeys.EMAIL] ?: "",
                name = prefs[PrefsKeys.NAME],
            )
        }
    }

    /**
     * Salva perfil no Firestore
     */
    suspend fun saveUserToFirestore(user: User) {
        try {
            firestore.collection("users")
                .document(user.uid)
                .set(user)
                .await()
        } catch (e: Exception) {
            throw Exception("Erro ao salvar no Firestore: ${e.message}")
        }
    }

    /**
     * Obtém perfil do Firestore
     */
    suspend fun getUserFromFirestore(uid: String): User? {
        return try {
            firestore.collection("users")
                .document(uid)
                .get()
                .await()
                .toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Remove dados locais do usuário
     */
    suspend fun clearUserData() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }

    suspend fun updateUserScore(uid: String, scoreToAdd: Long){
        try {
            usersCollection.document(uid).update("score",
                FieldValue.increment(scoreToAdd)).await()
        } catch (e: Exception){
            throw Exception("Erro ao atualizar o score: ${e.message}")
        }
    }

    suspend fun getRanking() : List<User>{
        return try {
            val snapshot = usersCollection
                .orderBy("score", Query
                    .Direction.DESCENDING)
                .limit(20)
                .get()
                .await()
            snapshot.toObjects(User::class.java)
        } catch (e: Exception){
            emptyList()
        }
    }
}