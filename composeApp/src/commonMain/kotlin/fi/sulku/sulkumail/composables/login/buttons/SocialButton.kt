package fi.sulku.sulkumail.composables.login.buttons

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun SocialButton(
    contentDescription: String,
    icon: DrawableResource,
    size: Dp,
    borderColor: Any,
    tint: Color,
    onClick: () -> Unit
) {
    //Handle different types of border colors
    val modifier = when (borderColor) {
        is Brush -> Modifier.border(1.dp, borderColor, RoundedCornerShape(100.dp))
        is Color -> Modifier.border(1.dp, borderColor,
            androidx.compose.foundation.shape.RoundedCornerShape(100.dp)
        )
        else -> Modifier
    }

    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.size(size),
            painter = painterResource(icon),
            contentDescription = contentDescription,
            tint = tint
        )
    }
}