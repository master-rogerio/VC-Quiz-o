package com.example.vcquizo.view.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {

    //Recebe o Status de Autentificado ou Nao Autentificado
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        auth.addAuthStateListener { firebaseAuth ->
            checkAuthState()
        }
        checkAuthState()
    }

    //convertida classe para private
    private fun checkAuthState() {
        if (auth.currentUser == null) {
            _authState.value = AuthState.Unauthenticated

        } else {
            _authState.value = AuthState.Authenticated
        }
    }

    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Preencha corretamente o Email e a Senha")
            return
        }

        _authState.value = AuthState.Loading //Carrega o Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Ocorreu uma falha, tente novamente.")
                }
            }
    }

    fun signup(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Preencha corretamente o Email e a Senha")
            return
        }

        _authState.value = AuthState.Loading //Carrega o Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.SignedUp
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Ocorreu uma falha, tente novamente.")
                }
            }
    }

    fun signout() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }

    fun resetState() {
        _authState.value = AuthState.Unauthenticated
    }

}

sealed class AuthState {
    object Authenticated : AuthState() // Estado de Autentificado
    object Unauthenticated : AuthState() // Estado de Nao Autentificado
    object Loading : AuthState() // Estado de Carregamento
    object SignedUp : AuthState() // Estado de Registro
    data class Error(val message: String) : AuthState() // Estado de Erro
}