package at.tauerntec.todo.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun AddTodoDialog(onAdd: (String) -> Unit, onDismiss: () -> Unit) {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                if (text.text.isNotBlank()) {
                    onAdd(text.text.trim())
                    onDismiss()
                }
            }) { Text("Hinzufügen") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss)
            { Text("Abbrechen")}
        },
        title = { Text("Neues TODO") },
        text = { OutlinedTextField(value = text, onValueChange = {text = it}, placeholder = { Text("Midn Hund geh...") }) }
    )
}