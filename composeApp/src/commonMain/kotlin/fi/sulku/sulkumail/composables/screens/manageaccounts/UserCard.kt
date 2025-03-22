package fi.sulku.sulkumail.composables.screens.manageaccounts

import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fi.sulku.sulkumail.auth.models.room.user.User
import fi.sulku.sulkumail.composables.content.DeleteButton

@Composable
fun UserCard(
    user: User,
    onDelete: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .hoverable(interactionSource)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = user.userInfo.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = user.userInfo.email,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            DeleteButton(
                iconDesc = "Delete User",
                interactionSource = interactionSource,
                onDelete = onDelete
            )
        }
    }
}