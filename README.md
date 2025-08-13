<div align="center">
<img src="extras/logo-ufu.png" alt="UFU Logo" width="800"/>
 </div>
 
# Trabalho da Disciplina Programação para Dispositivos Móveis
 
Quarto trabalho da discilpina Programação para Dispositivos Móveis do curso de Sistemas de Informação da Universidade Federal de Uberlândia


<div align="center">
  <img src="https://cdn-icons-png.flaticon.com/512/25/25374.png" alt="Android Logo" width="500"/>
</div>



## 🧾 Índice
aaa


## 📊 Diagrama de Arquitetura do Sistema

```mermaid
graph TD
    A[🔑 AWS Parameter Store] -->|Fornece credenciais| B[🛡️ IAM Role]
    B -->|Concede permissões| C[🖥️ EC2 Linux Ubuntu]
    C --> D[🕸️ Nginx]
    C --> E[🤖 Webhook Telegram]
    C --> F[📜 Script Bash]
    
    subgraph "Instância AWS EC2"
        D -->|Serve| G[📄 Página Web]
        E -->|Envia| H[📱 Notificações]
        F -->|Monitora| D
        F -->|Dispara| E
    end

    style A fill:#FF9900,color:#000
    style B fill:#232F3E,color:#FFF
    style C fill:#FF9900,color:#000

```

## 📖 Sobre o Aplicativo

Esse aplicativo possui as seguintes funcionalidades:

## ✅ Funcionalidades

- ✅ Autentificação com Email e Senha com o Google Firebase;
- ✅ Banco de Dados das Questões utilizando o Realtime Database do Google Firebase;
- ✅ Histórico de Performance



## 🛠️ Tecnologias Utilizadas

- [Android Studio](https://developer.android.com/)
- [Firebase](https://firebase.google.com/)
- [JetPack Compose](https://developer.android.com/compose)
- [Kotlin](https://kotlinlang.org/)




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
