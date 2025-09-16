package at.tauerntec.todo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.tauerntec.todo.data.TodoRepository
import at.tauerntec.todo.model.Todo
import at.tauerntec.todo.util.Result
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TodoViewModel(private val repo: TodoRepository = TodoRepository()) : ViewModel() {
    private val _todos = MutableStateFlow<Result<List<Todo>>>(Result.Loading)
    val todos: StateFlow<Result<List<Todo>>> = _todos

    init {
        viewModelScope.launch {
            repo.streamTodos().collect { list -> _todos.value = Result.Success(list) }
        }
    }

    fun add(title: String, description: String) = viewModelScope.launch { repo.add(title, description) }
    fun toggle(todo: Todo) = viewModelScope.launch { repo.toggle(todo.id, !todo.done) }
    fun delete(todo: Todo) = viewModelScope.launch { repo.delete(todo.id) }
}