package com.example.vcquizo.data

import com.google.firebase.ktx.Firebase
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.vcquizo.domain.model.User
import com.example.vcquizo.ui.util.QuizResult
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.PersistentCacheSettings
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
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


    suspend fun updateBestScore(uid: String, quizId: String, newScore: Long) {
        val userDocRef = usersCollection.document(uid)

        try {
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(userDocRef)
                val user = snapshot.toObject(User::class.java)
                    ?: throw Exception("Usuário não encontrado")

                val oldBestScore = user.bestScores[quizId] ?: 0L

                // Só faz algo se a nova pontuação for maior que a antiga
                if (newScore > oldBestScore) {
                    // 1. Calcula a diferença a ser adicionada na pontuação total
                    val scoreDifference = newScore - oldBestScore

                    // 2. Cria o novo mapa de melhores pontuações
                    val newBestScores = user.bestScores.toMutableMap()
                    newBestScores[quizId] = newScore

                    // 3. Na transação, atualiza o mapa e incrementa a pontuação total
                    transaction.update(userDocRef, "bestScores", newBestScores)
                    transaction.update(userDocRef, "score", FieldValue.increment(scoreDifference))
                }
                // Se a pontuação não for maior, a transação termina sem fazer nada.
            }.await()
        } catch (e: Exception) {
            throw Exception("Erro ao atualizar o melhor score: ${e.message}")
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

    suspend fun saveQuizResult(uid: String, result: QuizResult){
        val historyDocRef = usersCollection.document(uid)
            .collection("quizHistory")
            .document(result.quizTitle)

        try {
            android.util.Log.d("UserRepository", "Salvando resultado: ${result.quizTitle}, Score: ${result.score}")
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(historyDocRef)

                if(snapshot.exists()){
                    val existingScore = snapshot.getLong("score") ?: 0L
                    android.util.Log.d("UserRepository", "Documento existe. Score existente: $existingScore, Novo score: ${result.score}")
                    if(result.score > existingScore){
                        transaction.set(historyDocRef, result)
                        android.util.Log.d("UserRepository", "Score atualizado!")
                    } else {
                        android.util.Log.d("UserRepository", "Score não foi melhor, não atualizando")
                    }
                } else {
                    transaction.set(historyDocRef, result)
                    android.util.Log.d("UserRepository", "Novo documento criado!")
                }
            }.await()
            android.util.Log.d("UserRepository", "Resultado salvo com sucesso!")

        } catch (e: Exception){
            android.util.Log.e("UserRepository", "Erro ao salvar resultado: ${e.message}")
            throw Exception("Erro ao salvar o resultado do quiz: ${e.message}")
        }
    }

    suspend fun getQuizHistory(uid: String): List<QuizResult> {
        return try {
            android.util.Log.d("UserRepository", "Buscando histórico para usuário: $uid")
            val snapshot = usersCollection
                .document(uid)
                .collection("quizHistory")
                .orderBy("score", Query.Direction.DESCENDING)
                .get()
                .await()
            val results = snapshot.toObjects(QuizResult::class.java)
            android.util.Log.d("UserRepository", "Histórico encontrado: ${results.size} itens")
            results.forEach { result ->
                android.util.Log.d("UserRepository", "Item: ${result.quizTitle}, Score: ${result.score}")
            }
            results
        } catch (e: Exception) {
            android.util.Log.e("UserRepository", "Erro ao buscar histórico: ${e.message}")
            emptyList()
        }
    }
}