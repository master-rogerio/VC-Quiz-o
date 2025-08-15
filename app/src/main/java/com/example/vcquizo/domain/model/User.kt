/**
 * Modelo de dados do usuário para Firestore e armazenamento local
 * @property uid ID único do Firebase Auth
 * @property email Email do usuário
 * @property name Nome de exibição (opcional)
  */
data class User(
    val uid: String = "",
    val email: String = "",
    val name: String? = null,
) {
    // Construtor vazio necessário para o Firestore
    constructor() : this("", "", null)
}