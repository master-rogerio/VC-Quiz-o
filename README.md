<div align="center">
<img src="extras/logo-ufu.png" alt="UFU Logo" width="800"/>
 </div>
 
# Trabalho da Disciplina Programação para Dispositivos Móveis
 
Esse é o repositório para o quarto trabalho da discilpina Programação para Dispositivos Móveis do curso de Sistemas de Informação da Universidade Federal de Uberlândia. 
###### Por Luiz Fellipe Silva Lima, Eduardo Antônio da Silva e Rogério Anastácio

<br>
<div align="center">
  <img src="https://cdn-icons-png.flaticon.com/512/25/25374.png" alt="Android Logo" width="300"/>
</div>


![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-7F52FF?style=for-the-badge&logo=kotlin)
![Compose](https://img.shields.io/badge/UI-Jetpack_Compose-4285F4?style=for-the-badge&logo=jetpackcompose)
![Firebase](https://img.shields.io/badge/Backend-Firebase-FFCA28?style=for-the-badge&logo=firebase)


## 🧾 Índice
* [Diagrama de Arquitetura do Sistema](#-diagrama-de-arquitetura-do-sistema)
* [Sobre o Aplicativo](#-sobre-o-aplicativo)
* [Funcionalidades](#-funcionalidades)
* [Tecnologias Utilizadas](#-tecnologias-utilizadas)



## 📊 Diagrama de Arquitetura do Sistema

```mermaid
graph TD
    subgraph "Android App (Cliente)"
        UI["📱 UI (Jetpack Compose)"]
        NAV["🗺️ Navigation"]
        VM["🧠 ViewModels"]
        REPO["📦 UserRepository"]
        DS["💾 DataStore (Cache Local)"]
    end

    subgraph "Backend (Firebase)"
        AUTH["🔥 Firebase Authentication"]
        FS["📄 Cloud Firestore"]
    end

    UI --> NAV
    NAV --> VM
    VM --> REPO
    REPO --> DS
    REPO --> FS
    VM --> AUTH
```

## 📖 Sobre o Aplicativo

Este é um aplicativo de quiz para Android, desenvolvido em Kotlin com Jetpack Compose. O aplicativo permite que os usuários se cadastrem, façam login, respondam a quizzes de diferentes categorias, acompanhem seu histórico de desempenho e vejam um ranking de pontuações. 
O aplicativo foi projetado para funcionar tanto online quanto offline, utilizando o Firebase para serviços de backend e o DataStore para cache de dados locais.

## ✅ Funcionalidades

- ✅ Autenticação de Usuário: Sistema completo de cadastro e login com e-mail e senha, utilizando o Firebase Authentication.
- ✅ Navegação Intuitiva: Navegação entre as telas de login, cadastro, home, quiz e resultados.
- ✅ Experiência Offline: O aplicativo permite o login offline e armazena os dados do usuário localmente, garantindo o acesso mesmo sem conexão com a internet.
- ✅ Interface Moderna: A interface do usuário é construída com Jetpack Compose, proporcionando uma experiência de usuário moderna e reativa.
- ✅ Histórico de Performance: Os usuários podem visualizar seu histórico de quizzes concluídos, incluindo pontuação, precisão e tempo gasto.
- ✅ Ranking de Jogadores: Uma tela de ranking exibe a pontuação dos melhores jogadores.



## 🛠️ Tecnologias Utilizadas

-   **[Kotlin](https://kotlinlang.org/):** Linguagem de programação oficial para o desenvolvimento Android.
-   **[Jetpack Compose](https://developer.android.com/jetpack/compose):** Kit de ferramentas moderno para a criação de interfaces de usuário nativas do Android.
-   **[Firebase Authentication](https://firebase.google.com/docs/auth):** Para gerenciar a autenticação de usuários.
-   **[Cloud Firestore](https://firebase.google.com/docs/firestore):** Banco de dados NoSQL para armazenar os dados do usuário.
-   **[DataStore](https://developer.android.com/topic/libraries/architecture/datastore):** Solução de armazenamento de dados que permite salvar pares de chave-valor ou objetos tipados com buffers de protocolo.
-   **[Android ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel):** Para gerenciar os dados da interface do usuário de maneira consciente do ciclo de vida.
-   **[Android Navigation](https://developer.android.com/guide/navigation):** Para lidar com a navegação entre as telas do aplicativo.
-   **[Coroutines](https://kotlinlang.org/docs/coroutines-overview.html):** Para gerenciar tarefas assíncronas.




<!--

  
- [MongoDB](https://www.mongodb.com/)
- Outros...

## 📦 Instalação

```bash
# Clone o repositório
git clone https://github.com/seunome/seuprojeto.git

# Acesse a pasta
cd seuprojeto

# Instale as dependências
npm install

# Inicie o projeto
npm start
-->
