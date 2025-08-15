
package com.example.vcquizo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.vcquizo.data.UserRepository
import com.example.vcquizo.domain.model.User
import com.example.vcquizo.ui.home.HomeScreen
import com.example.vcquizo.ui.quiz.QuizScreen
import com.example.vcquizo.ui.result.ResultScreen
// Remova as referências às pages de Login e Signup por enquanto
import com.example.vcquizo.pages.LoginPage
import com.example.vcquizo.pages.SignupPage
import com.example.vcquizo.view.model.AuthViewModel

@Composable
fun MyAppNavigation(modifier: Modifier, authViewModel: AuthViewModel, userRepository: UserRepository) { // O ViewModel será usado no futuro
    val navController = rememberNavController()

    // FUTURAMENTE: Trocar startDestination para "login" ou uma tela de "splash"
    // que verificará o authState.
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        // Rota sem e-mail (usada no start e quando abrir o app)
        composable("login") {
            LoginPage( navController = navController, authViewModel = authViewModel, initialEmail = "")
        }

        composable("signup") {
            SignupPage(navController = navController, authViewModel = authViewModel)
        }

        composable("home") {
            HomeScreen(navController = navController, authViewModel = authViewModel)
        }


        // Rota para voltar a tela de login após Cadastrar Email.
        composable(
            route = "login/{email}",
            arguments = listOf(
                navArgument("email") { defaultValue = "" }
            )
        ) { backStackEntry ->
            val passedEmail = backStackEntry.arguments?.getString("email").orEmpty()
            LoginPage(
                navController = navController,
                authViewModel = authViewModel,
                initialEmail = passedEmail
            )
        }


        // Rota para a tela de quiz, esperando um 'quizId' como argumento
        composable("quiz/{quizId}") { backStackEntry ->
            val quizId = backStackEntry.arguments?.getString("quizId")
            // FUTURAMENTE: Usar o quizId para buscar o quiz do banco de dados.
            // Por enquanto, usamos o MockData.
            if (quizId != null) {
                QuizScreen(navController = navController, quizId = quizId)
            }
        }

        // Rota para a tela de resultado
        composable("result/{score}/{accuracy}/{time}/{cameFromHistory}") { backStackEntry ->
            val score = backStackEntry.arguments?.getString("score")?.toInt() ?: 0
            val accuracy = backStackEntry.arguments?.getString("accuracy")?.toFloat() ?: 0f
            val time = backStackEntry.arguments?.getString("time") ?: "00:00"
            val cameFromHistory = backStackEntry.arguments?.getString("cameFromHistory")?.toBoolean() ?: false

            ResultScreen(
                navController = navController,
                score = score,
                accuracy = accuracy,
                timeTaken = time,
                cameFromHistory = cameFromHistory
            )
        }
    }
}