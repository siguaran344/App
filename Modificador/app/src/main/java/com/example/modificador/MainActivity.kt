package com.example.modificador

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.yourappname.R // Cambia esto por el nombre de tu paquete

@Composable
fun ColorModifierImage() {
    var saturation by remember { mutableStateOf(1f) } // Saturación inicial

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        // Imagen con el filtro de color aplicado
        Image(
            painter = painterResource(id = R.drawable.your_image), // Cambia 'your_image' por tu recurso de imagen
            contentDescription = "Modificable Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            colorFilter = ColorFilter.colorMatrix(colorMatrixFromSaturation(saturation))
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Slider para ajustar la saturación
        Text(text = "Ajusta la saturación:")
        Slider(
            value = saturation,
            onValueChange = { saturation = it },
            valueRange = 0f..2f,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ColorModifierImage()
        }
}

// Función para crear un ColorMatrix basado en la saturación
fun colorMatrixFromSaturation(saturation: Float): androidx.compose.ui.graphics.ColorMatrix {
    val colorMatrix = ColorMatrix()
    colorMatrix.setSaturation(saturation)
    return androidx.compose.ui.graphics.ColorMatrix(colorMatrix.array())
}