package fi.sulku.sulkumail.composables.login.fields

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import fi.sulku.sulkumail.composables.login.Validation

@Composable
fun PasswordField(input: String, passwordVisible: MutableState<Boolean>, onValueChange: (String) -> Unit) {
    InputField(
        label = "Password",
        input = input,
        leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Password") },
        trailingIcon = {
            val image: ImageVector = if (passwordVisible.value) {
                Icons.Filled.Visibility
            } else {
                Icons.Filled.VisibilityOff
            }
            IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                Icon(imageVector = image, contentDescription = null)
            }
        },
        onValueChange = onValueChange,
        visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        validations = arrayOf(
            Validation("Password must be at least 8 characters long") { it.length >= 8 },
            Validation("Password must contain at least one uppercase letter") { it.any { it.isUpperCase() } },
            Validation("Password must contain at least one lowercase letter") { it.any { it.isLowerCase() } },
            Validation("Password must contain at least one digit") { it.any { it.isDigit() } },
            Validation("Password must contain at least one special character") {
                it.any { "!@#$%^&*()_+-=[]|,./?><".contains(it) }
            }
        ),
    )
}