package at.tauerntec.todo.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import at.tauerntec.todo.ui.AuthViewModel

@Composable
fun RegisterScreen(
    vm: AuthViewModel,
    onRegistered: () -> Unit,
    onNavigateLogin: () -> Unit
) {
    val state by vm.state.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(Modifier.padding(24.dp)) {
        Text("Create account", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, singleLine = true)
        OutlinedTextField(value = password, onValueChange = { email = it }, label = { Text("Passwort") }, visualTransformation = PasswordVisualTransformation(), singleLine = true)
        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            vm.register(email.trim(), password)
            onRegistered()
        }, enabled = state !is at.tauerntec.todo.util.Result.Loading) { Text("Register") }
        TextButton(onClick = onNavigateLogin) { Text("Zurück zum Login") }

        when (state) {
            is at.tauerntec.todo.util.Result.Error -> Text((state as at.tauerntec.todo.util.Result.Error).message, color = MaterialTheme.colorScheme.error)
            is at.tauerntec.todo.util.Result.Loading -> LinearProgressIndicator()
            else -> { }
        }
    }
}