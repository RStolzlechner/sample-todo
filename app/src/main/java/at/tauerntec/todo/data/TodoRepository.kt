package at.tauerntec.todo.data

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import at.tauerntec.todo.model.Todo
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class TodoRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    private fun userTodos() = db
        .collection("users")
        .document(auth.currentUser!!.uid)
        .collection("todos")

    fun streamTodos(): Flow<List<Todo>> = callbackFlow {
        val listener = userTodos()
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener {snap, err ->
                if(err !=null) { close(err); return@addSnapshotListener }
                val list = snap?.documents?.map { doc ->
                    Todo(
                        id = doc.id,
                        title = doc.getString("title") ?: "",
                        description = doc.getString("description")?:"",
                        done = doc.getBoolean("done")?: false,
                        createdAt = doc.getTimestamp("createdAt") ?: Timestamp.now()
                    )
                } ?: emptyList()
                trySend(list)
            }
        awaitClose { listener.remove() }
    }

    suspend fun add(title: String, description: String) {
        userTodos().add(
            mapOf(
                "title" to title,
                "description" to description,
                "done" to false,
                "createdAt" to FieldValue.serverTimestamp()
            )
        ).await()
    }

    suspend fun toggle(id: String, done: Boolean) {
        userTodos().document(id).update("done", done).await()
    }

    suspend fun delete(id: String) {
        userTodos().document(id).delete().await()
    }
}