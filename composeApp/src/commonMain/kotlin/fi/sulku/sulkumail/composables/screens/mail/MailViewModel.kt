package fi.sulku.sulkumail.composables.screens.mail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fi.sulku.sulkumail.data.auth.models.Folder
import fi.sulku.sulkumail.data.auth.models.room.MailEntity
import fi.sulku.sulkumail.data.auth.models.room.User
import fi.sulku.sulkumail.data.repositories.MailRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MailViewModel(private val mailRepo: MailRepository) : ViewModel() {

    fun getMails(user: User): Flow<List<MailEntity>> = mailRepo.getMails(user)

    fun fetchMail(user: User, folder: Folder) {
        viewModelScope.launch {
            mailRepo.fetchMails(user, folder)
        }
    }

    fun trashMail(user: User, mail: MailEntity) {
        viewModelScope.launch {
            mailRepo.trashMail(user, mail)
        }
    }
}