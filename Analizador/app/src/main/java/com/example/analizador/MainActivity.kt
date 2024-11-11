package com.example.analizador

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import androidx.compose.ui.graphics.ImageBitmap
import androidx.core.graphics.drawable.toBitmap

@Composable
fun ImageAnalyzerScreen() {
    val context = LocalContext.current
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var detectedText by remember { mutableStateOf("") }

    // Launcher para seleccionar una imagen de la galería
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val bitmap = context.contentResolver.openInputStream(it)?.use { stream ->
                android.graphics.BitmapFactory.decodeStream(stream)
            }
            bitmap?.let {
                imageBitmap = it.asImageBitmap()
                analyzeImage(it) { text ->
                    detectedText = text
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Botón para seleccionar imagen de la galería
        Button(onClick = { galleryLauncher.launch("image/*") }) {
            Text("Seleccionar Imagen")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar la imagen seleccionada
        imageBitmap?.let { bitmap ->
            Image(
                bitmap = bitmap,
                contentDescription = "Imagen Seleccionada",
                modifier = Modifier.size(200.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar el texto detectado
        Text(text = "Texto Detectado:")
        Text(text = detectedText, modifier = Modifier.padding(8.dp))
    }
}

// Función para analizar la imagen y detectar texto
private fun analyzeImage(bitmap: Bitmap, onTextFound: (String) -> Unit) {
    val image = InputImage.fromBitmap(bitmap, 0)
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    recognizer.process(image)
        .addOnSuccessListener { visionText ->
            // Obtener el texto detectado
            val detectedText = visionText.text
            onTextFound(detectedText)
        }
        .addOnFailureListener { e ->
            onTextFound("Error al analizar la imagen: ${e.message}")
        }
}

// Actividad principal para mostrar la pantalla del analizador
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImageAnalyzerScreen()
        }
    }
}