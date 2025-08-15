package com.example.vcquizo.pages

import android.graphics.Color.rgb
import android.widget.Space
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LightbulbCircle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.sharp.Lightbulb
import androidx.compose.material.icons.twotone.LightbulbCircle
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vcquizo.R
import com.example.vcquizo.view.model.AuthState
import com.example.vcquizo.view.model.AuthViewModel
import java.nio.file.WatchEvent
import kotlin.math.round


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel, initialEmail: String = "") {

    var email by remember {
        mutableStateOf(initialEmail)
    }
    var password by remember {
        mutableStateOf("")
    }

    val passwordFocus = remember { FocusRequester() }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    val focusManager = LocalFocusManager.current

    val isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val isPasswordValid = password.length in 6..20

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> {
                navController.navigate("home")
            }
            is AuthState.Error -> Toast.makeText(context,
                (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }
    // Se já veio com e-mail preenchido, foca na senha
    LaunchedEffect(initialEmail) {
        if (initialEmail.isNotEmpty()) {
            passwordFocus.requestFocus()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)
    ){
//        Image(
//            painter = painterResource(id = R.drawable.fundo), // Imagem na pasta res/drawable/fundo
//            contentDescription = null,
//            contentScale = ContentScale.Crop, // preenche toda a tela
//            modifier = Modifier.fillMaxSize()
//        )




    Column (
        modifier = modifier
            .fillMaxSize()
            .clickable(
                indication = null, // sem efeito de clique
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus() // fecha o teclado
            }
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Column(modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Icon(
                imageVector = Icons.Default.LightbulbCircle,
                contentDescription = "Logo",
                tint = MaterialTheme.colorScheme.primary,
                modifier = modifier.size(175.dp)
            )
            Spacer(modifier = modifier.height(5.dp))

            Text(text = "Login",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 0.005.sp
            )
        }

        Spacer(modifier = Modifier.height(5.dp))
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it.trim()
            },
            label = {
                Text(text = "E-mail",

                )
            },
            maxLines = 1,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest, // 50% transparente
                unfocusedBorderColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedTextColor = MaterialTheme.colorScheme.primary,

            ),
            shape = RoundedCornerShape(20.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { passwordFocus.requestFocus()}
            )

        )

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = {
                if (it.length <= 20) password = it // impede mais de 20 caracteres
            },
            label = { Text("Senha",

            )
                    },

            maxLines = 1, // Limita em 1 linha
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest, // 50% transparente
                unfocusedBorderColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedTextColor = MaterialTheme.colorScheme.primary,
            ),
            shape = RoundedCornerShape(20.dp),
            visualTransformation = if (passwordVisible)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    // mesmo comportamento do botão
                    if (isEmailValid && isPasswordValid && authState.value != AuthState.Loading) {
                        focusManager.clearFocus() // fecha o teclado
                        authViewModel.login(email, password)
                    }
                }
            ),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = null,
                        tint = MaterialTheme.colorScheme.surfaceContainerHighest)
                }
            },
            modifier = Modifier.focusRequester(passwordFocus),
            supportingText = {
                if (password.isNotEmpty() && password.length !in 6..20) {
                    Text(
                        text = "A senha é de 6 à 20 caracteres.",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            authViewModel.login(email, password)
        },
            enabled = isEmailValid && isPasswordValid && authState.value != AuthState.Loading
            ) {
            if (authState.value == AuthState.Loading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(text = "Login",
                    fontWeight = FontWeight.Light)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = {
            navController.navigate("signup")
        }) {
            Text(text = "Não tem uma conta? Cadastre-se",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Light,
                fontSize = 14.sp
            )
        }
    }
    }

}
