package com.example.vcquizo.view.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.vcquizo.data.UserRepository
import com.example.vcquizo.domain.model.User
import com.example.vcquizo.util.ConnectivityUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch



class AuthViewModel (private val userRepository: UserRepository, application: Application) : AndroidViewModel(application) {

    private val auth : FirebaseAuth = Firebase.auth
    private val context = getApplication<Application>().applicationContext


    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        // Listener para observar autentificação. Ele é disparado na inicialização e sempre que o estado do usuário mudar.
        auth.addAuthStateListener { firebaseAuth ->
            checkAuthState(firebaseAuth)
        }
    }


    private fun checkAuthState(firebaseAuth: FirebaseAuth) {
        if (firebaseAuth.currentUser != null) {
            // O Firebase confirma que o usuário está logado (online ou cache do token).
            _authState.value = AuthState.Authenticated
        } else {
            // Verifica-se isso ocorre porque está offline e tem um usuário local.
            viewModelScope.launch {
                val isOffline = !ConnectivityUtil.isOnline(context)
                val localUserExists = userRepository.userLocal.firstOrNull() != null

                if (isOffline && localUserExists) {
                    // Cenário: App iniciado offline, e tem um usuário salvo.
                    // Usa os dados locais para permitir o acesso.
                    _authState.value = AuthState.Authenticated
                } else {
                    // Cenário: Usuário genuinamente deslogado ou online sem sessão.
                    _authState.value = AuthState.Unauthenticated
                }
            }
        }
    }



    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Preencha corretamente o Email e a Senha")
            return
        }
        _authState.value = AuthState.Loading //Estado de Loading

        if (ConnectivityUtil.isOnline(context)) {
            // Lógica para login ONLINE
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        auth.currentUser?.uid?.let { uid ->
                            viewModelScope.launch {
                                // Após login online, busca os dados mais recentes do Firestore
                                // e salva os localmente para o próximo acesso, se offline.
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
                    _authState.value = AuthState.Authenticated
                } else {
                    // Nenhum usuário local encontrado com o e-mail com sessão ativa.
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

        _authState.value = AuthState.Loading //Estado de Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Cria perfil do usuário no Banco de Dados Local e Online, após cadastro.
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