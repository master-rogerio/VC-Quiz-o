import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
com.example.app/
├── data/
│   ├── UserRepository.kt  # Combina local e remoto
├── model/
│   └── User.kt            # Modelo de dados
└── viewmodel/
├── AuthViewModel.kt   # Autenticação
└── UserProfileViewModel.kt # Perfil do usuário


 * ViewModel que gerencia a lógica de apresentação do perfil do usuário
 *
 * @param userRepo Repositório injetado para acesso aos dados
 */
class ProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    /**
     * Carrega perfil do usuário (local + remoto)
     */
    fun loadProfile(uid: String) {
        viewModelScope.launch {
            try {
                // Primeiro mostra dados locais
                userRepository.userLocal.collect { localUser ->
                    if (localUser != null) {
                        _user.value = localUser
                    }

                    // Depois sincroniza com Firestore
                    val remoteUser = userRepository.getUserFromFirestore(uid)
                    remoteUser?.let {
                        userRepository.saveUserLocal(it)
                        _user.value = it
                    }
                }
            } catch (e: Exception) {
                _error.value = "Erro ao carregar perfil: ${e.message}"
            }
        }
    }

    /**
     * Atualiza nome do usuário
     */
    fun updateName(uid: String, newName: String) {
        viewModelScope.launch {
            try {
                val currentUser = _user.value ?: return@launch
                val updatedUser = currentUser.copy(name = newName)

                userRepository.saveUserToFirestore(updatedUser)
                userRepository.saveUserLocal(updatedUser)
                _user.value = updatedUser
            } catch (e: Exception) {
                _error.value = "Erro ao atualizar nome: ${e.message}"
            }
        }
    }
}