package at.tauerntec.todo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.tauerntec.todo.data.AuthRepository
import at.tauerntec.todo.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repo: AuthRepository = AuthRepository()): ViewModel() {
    private val _state = MutableStateFlow<Result<Unit>>(Result.Success(Unit))
    val state: StateFlow<Result<Unit>> = _state

    fun isLoggedIn() = repo.currentUser != null

    fun login(email: String, password: String) = launch { repo.login(email, password)}
    fun register(email: String, password: String) = launch { repo.register(email, password) }
    fun logout() = repo.logout()

    private fun launch(block: suspend () -> Unit) {
        viewModelScope.launch {
            _state.value = Result.Loading
            try { block(); _state.value = Result.Success(Unit) }
            catch (t: Throwable) { _state.value = Result.Error(t.message ?: "Unknown error", t) }
        }
    }
}