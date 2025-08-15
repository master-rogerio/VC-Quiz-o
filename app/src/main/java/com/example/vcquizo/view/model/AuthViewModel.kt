package com.example.vcquizo.view.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vcquizo.data.UserRepository
import com.example.vcquizo.domain.model.User
import com.example.vcquizo.util.ConnectivityUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context

class AuthViewModel (private val userRepository: UserRepository, application: Application) : AndroidViewModel(application) {

    private val auth : FirebaseAuth = Firebase.auth
    private val context = getApplication<Application>().applicationContext
//class AuthViewModel (private val userRepository: UserRepository) : ViewModel() {

    //Recebe o Status de Autentificado ou Nao Autentificado
  //  private val auth : FirebaseAuth = Firebase.auth//.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        checkAuthState()
    }


    private fun checkAuthState() {
        if (auth.currentUser == null) {
            // Se não há usuário do Firebase, mas estamos offline,
            // poderíamos verificar um usuário local para manter a sessão offline.
            // Por enquanto, vamos manter simples.
            viewModelScope.launch {
                if (!ConnectivityUtil.isOnline(context) && userRepository.userLocal.firstOrNull() != null) {
                    _authState.value = AuthState.Authenticated
                } else if (auth.currentUser != null) {
                    _authState.value = AuthState.Authenticated
                }
                else {
                    _authState.value = AuthState.Unauthenticated
                }
            }
        } else {
            _authState.value = AuthState.Authenticated
        }
    }



    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Preencha corretamente o Email e a Senha")
            return
        }
        _authState.value = AuthState.Loading

        if (ConnectivityUtil.isOnline(context)) {
            // Lógica para login ONLINE
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        auth.currentUser?.uid?.let { uid ->
                            viewModelScope.launch {
                                val user = userRepository.getUserFromFirestore(uid)
                                user?.let { userRepository.saveUserLocal(it) }
                                _authState.value = AuthState.Authenticated
                            }
                        }
                    } else {
                        _authState.value = AuthState.Error(task.exception?.message ?: "Falha no login.")
                    }
                }
        } else {
            // Lógica para login OFFLINE
            viewModelScope.launch {
                val localUser = userRepository.getUserByEmailLocal(email)
                if (localUser != null) {
                    // Usuário encontrado localmente. Autenticação offline bem-sucedida.
                    // Nota: Não estamos verificando a senha aqui.
                    _authState.value = AuthState.Authenticated
                } else {
                    // Nenhum usuário local encontrado.
                    _authState.value = AuthState.Error("Sem conexão. Faça login online pelo menos uma vez.")
                }
            }
        }
    }

    fun signup(email: String, password: String,name: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Preencha corretamente o Email e a Senha")
            return
        }

        _authState.value = AuthState.Loading //Carrega o Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Cria perfil do usuário após cadastro
                    auth.currentUser?.let { firebaseUser ->
                        viewModelScope.launch {
                            val user = User(
                                uid = firebaseUser.uid,
                                email = email,
                                name = name
                            )
                            userRepository.saveUserToFirestore(user)
                            userRepository.saveUserLocal(user)
                        }
                    }
                    _authState.value = AuthState.SignedUp
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Ocorreu uma falha, tente novamente.")
                }
            }
    }

    fun signout() {
        viewModelScope.launch {
            userRepository.clearUserData()
        }
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