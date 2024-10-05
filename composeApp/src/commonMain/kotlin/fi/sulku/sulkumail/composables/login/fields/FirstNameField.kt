package fi.sulku.sulkumail.composables.login.fields

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun FirstNameField(input: String, onValueChange: (String) -> Unit) {
    InputField(
        label = "First Name",
        input = input,
        leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "First name") },
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )
}