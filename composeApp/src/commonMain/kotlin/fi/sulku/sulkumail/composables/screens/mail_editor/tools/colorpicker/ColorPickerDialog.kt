package fi.sulku.sulkumail.composables.screens.mail_editor.tools.colorpicker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController


@ExperimentalComposeUiApi
@Composable
fun ColorPickerDialog(
    onDismissRequest: () -> Unit,
    onPickedColor: (Color) -> Unit,
    properties: DialogProperties = DialogProperties()
) {
    val controller = rememberColorPickerController()
    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = properties
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(32.dp))
                .background(Color.White)
        ) {
            Column {
                HsvColorPicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(450.dp)
                        .padding(10.dp),
                    controller = controller,
                    onColorChanged = { onPickedColor(it.color) }
                )
                AlphaSlider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .height(35.dp),
                    borderRadius = 6.dp,
                    borderSize = 5.dp,
                    borderColor = Color.LightGray,
                    controller = controller,
                )
                BrightnessSlider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .height(35.dp),
                    controller = controller,
                )
                /*        AlphaTile(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(6.dp)),
                            controller = controller
                        )*/
            }
        }
    }
}