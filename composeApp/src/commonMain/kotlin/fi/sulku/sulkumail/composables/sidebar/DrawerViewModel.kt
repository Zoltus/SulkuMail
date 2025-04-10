package fi.sulku.sulkumail.composables.sidebar

import androidx.lifecycle.ViewModel
import fi.sulku.sulkumail.data.auth.models.Folder
import fi.sulku.sulkumail.data.auth.models.room.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DrawerViewModel : ViewModel() {

    private val _selectedUserFolder = MutableStateFlow<Pair<User, Folder>?>(null)
    val selectedUserFolder = _selectedUserFolder.asStateFlow()

    private val _expandedUsers = MutableStateFlow<List<User>>(emptyList())
    val expandedUsers = _expandedUsers.asStateFlow()

    fun selectFolder(user: User, folder: Folder) {
        _selectedUserFolder.value = user to folder
    }

    //todo cleanup
    fun toggleUserExpansion(user: User) {
        _expandedUsers.value = if (_expandedUsers.value.contains(user)) {
            _expandedUsers.value - user
        } else {
            _expandedUsers.value + user
        }
    }
}