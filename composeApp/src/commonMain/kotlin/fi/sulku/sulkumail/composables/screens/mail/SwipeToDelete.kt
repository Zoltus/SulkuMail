package fi.sulku.sulkumail.composables.screens.mail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.delay

@Composable
fun SwipeToDelete(
    onDelete: () -> Unit,
    animationDuration: Int = 500,
    content: @Composable () -> Unit
) {
    val isVisible = remember { mutableStateOf(true) }
    val swipeState = rememberSwipeToDismissBoxState(positionalThreshold = { totalDistance -> totalDistance * 0.5f })
    val backgroundAppearance = getBackgroundAppearance(swipeState.dismissDirection)

    AnimatedDismissContent(
        isVisible = isVisible.value,
        animationDuration = animationDuration,
        swipeState = swipeState,
        backgroundAppearance = backgroundAppearance,
        content = content
    )

    HandleSwipeActions(
        swipeState = swipeState,
        isVisible = isVisible,
        animationDuration = animationDuration,
        onDelete = onDelete
    )
}

@Composable
private fun AnimatedDismissContent(
    isVisible: Boolean,
    animationDuration: Int,
    swipeState: SwipeToDismissBoxState,
    backgroundAppearance: BackgroundAppearance,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        SwipeToDismissBox(
            modifier = Modifier.animateContentSize(),
            state = swipeState,
            backgroundContent = {
                SwipeBackground(backgroundAppearance)
            }
        ) {
            content()
        }
    }
}


@Composable
private fun getBackgroundAppearance(dismissDirection: SwipeToDismissBoxValue): BackgroundAppearance {
    return when (dismissDirection) {
        SwipeToDismissBoxValue.EndToStart -> BackgroundAppearance(
            icon = Icons.Outlined.Delete,
            alignment = Alignment.CenterEnd,
            color = MaterialTheme.colorScheme.errorContainer
        )

        SwipeToDismissBoxValue.StartToEnd -> BackgroundAppearance(
            icon = Icons.Outlined.Edit,
            alignment = Alignment.CenterStart,
            color = Color.Green.copy(alpha = 0.3f)
        )

        SwipeToDismissBoxValue.Settled -> BackgroundAppearance(
            icon = Icons.Outlined.Delete,
            alignment = Alignment.CenterEnd,
            color = MaterialTheme.colorScheme.errorContainer
        )
    }
}


@Composable
private fun SwipeBackground(appearance: BackgroundAppearance) {
    Box(
        contentAlignment = appearance.alignment,
        modifier = Modifier
            .fillMaxSize()
            .background(appearance.color)
    ) {
        Icon(
            modifier = Modifier.minimumInteractiveComponentSize(),
            imageVector = appearance.icon,
            contentDescription = null
        )
    }
}

@Composable
private fun HandleSwipeActions(
    swipeState: SwipeToDismissBoxState,
    animationDuration: Int,
    onDelete: () -> Unit,
    isVisible: MutableState<Boolean>
) {
    when (swipeState.currentValue) {
        SwipeToDismissBoxValue.EndToStart -> {
            LaunchedEffect(key1 = true) {
                isVisible.value = false
                delay(animationDuration.toLong())
                onDelete()
            }
        }

        SwipeToDismissBoxValue.StartToEnd -> {
            LaunchedEffect(swipeState) {
                swipeState.snapTo(SwipeToDismissBoxValue.Settled)
            }
        }

        SwipeToDismissBoxValue.Settled -> {}
    }
}

private data class BackgroundAppearance(
    val icon: ImageVector,
    val alignment: Alignment,
    val color: Color
)