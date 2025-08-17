
package com.example.vcquizo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.vcquizo.ui.home.HomeScreen
import com.example.vcquizo.ui.quiz.QuizScreen
import com.example.vcquizo.ui.result.ResultScreen
import com.example.vcquizo.pages.LoginPage
import com.example.vcquizo.pages.SignupPage
import com.example.vcquizo.view.model.AuthViewModel

@Composable
fun MyAppNavigation(modifier: Modifier, authViewModel: AuthViewModel) { // O ViewModel será usado no futuro
    val navController = rememberNavController()

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
            if (quizId != null) {
                QuizScreen(navController = navController, quizId = quizId)
            }
        }

        // Rota para a tela de resultado
        composable("result/{score}/{accuracy}/{time}/{cameFromHistory}?quizId={quizId}",
            arguments = listOf(
                navArgument("quizId") { 
                    nullable = true 
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val score = backStackEntry.arguments?.getString("score")?.toInt() ?: 0
            val accuracy = backStackEntry.arguments?.getString("accuracy")?.toFloat() ?: 0f
            val time = backStackEntry.arguments?.getString("time") ?: "00:00"
            val cameFromHistory = backStackEntry.arguments?.getString("cameFromHistory")?.toBoolean() ?: false
            val quizId = backStackEntry.arguments?.getString("quizId")

            ResultScreen(
                navController = navController,
                score = score,
                accuracy = accuracy,
                timeTaken = time,
                cameFromHistory = cameFromHistory,
                quizId = quizId
            )
        }
    }
}