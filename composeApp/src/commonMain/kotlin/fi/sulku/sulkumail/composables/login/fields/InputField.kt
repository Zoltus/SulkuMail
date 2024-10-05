package fi.sulku.sulkumail.composables.login.fields

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.VisualTransformation
import fi.sulku.sulkumail.composables.login.Validation

@Composable
fun InputField(
    label: String,
    input: String,
    leadingIcon: @Composable (() -> Unit)?,
    trailingIcon: @Composable (() -> Unit)? = null,
    onValueChange: (String) -> Unit,
    visualTransformation: VisualTransformation = VisualTransformation.Companion.None,
    keyboardOptions: KeyboardOptions,
    vararg validations: Validation
) {
    val validation: Validation? = validations.find { !it.validate(input) }
    val errorMsg = validation?.errorMsg
    val isValid = errorMsg != null && input.isNotEmpty()

    TextField(
        value = input,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        isError = isValid,
        singleLine = true
    )
    if (isValid) {
        Text(validation!!.errorMsg, color = MaterialTheme.colorScheme.error) // todo remove !!
    }
}