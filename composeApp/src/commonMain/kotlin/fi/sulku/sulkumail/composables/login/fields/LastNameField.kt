package fi.sulku.sulkumail.composables.login.fields

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
import fi.sulku.sulkumail.composables.login.fields.InputField

@Composable
fun LastNameField(input: String, onValueChange: (String) -> Unit) {
    InputField(
        label = "Last Name",
        input = input,
        leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "Last Name") },
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )
}