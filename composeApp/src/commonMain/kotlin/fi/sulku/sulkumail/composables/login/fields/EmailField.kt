package fi.sulku.sulkumail.composables.login.fields

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
import fi.sulku.sulkumail.composables.login.Validation

@Composable
fun EmailField(input: String, onValueChange: (String) -> Unit) {
    InputField(
        label = "Email",
        input = input,
        leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Email") },
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        validations = arrayOf(
            Validation("Email is not valid") {
                input.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex())
            }
        )
    )
}