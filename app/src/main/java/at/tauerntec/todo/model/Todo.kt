package at.tauerntec.todo.model

import com.google.firebase.Timestamp

data class Todo(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val done: Boolean = false,
    val createdAt: Timestamp = Timestamp.now()
)