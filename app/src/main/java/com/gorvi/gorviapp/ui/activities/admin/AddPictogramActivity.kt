package com.gorvi.gorviapp.ui.activities

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import com.gorvi.gorviapp.GorviApp
import com.gorvi.gorviapp.ui.viewmodel.PictogramViewModelFactory
import com.gorvi.gorviapp.data.Pictogram
import com.gorvi.gorviapp.ui.viewmodel.PictogramViewModel
import java.io.File
import java.io.FileOutputStream

class AddPictogramActivity : AppCompatActivity() {

    private val viewModel: PictogramViewModel by viewModels {
        val app = application as GorviApp
        PictogramViewModelFactory(app.repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AddPictogramScreen()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AddPictogramScreen() {
        var labelText by remember { mutableStateOf("") }
        var fileName by remember { mutableStateOf<String?>(null) }

        val context = LocalContext.current

        val imagePickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                // Copy the image to internal storage and get the file path
                val internalFilePath = copyImageToInternalStorage(uri, context)
                // Update the fileName state with the internal file path
                fileName = internalFilePath
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            // Text field for label input
            OutlinedTextField(
                value = labelText,
                onValueChange = { labelText = it },
                label = { Text("Enter Label") }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Button to select an image from the gallery
            Button(onClick = {
                imagePickerLauncher.launch("image/*")
            }) {
                Text(text = "Select Image")
            }
            Spacer(modifier = Modifier.height(8.dp))

            if (fileName != null) {
                Text("Selected file: $fileName")
            }

            Button(onClick = {
                // Code to get image path and label
                val imagePath = fileName ?: ""
                val label = labelText
                viewModel.insert(Pictogram(imagePath=imagePath, label=label))

                finish()
            }) {
                Text(text = "Save")
            }
        }
    }

    private fun copyImageToInternalStorage(uri: Uri, context: Context): String? {
        val inputStream = context.contentResolver.openInputStream(uri)
        inputStream?.let { inputStream ->
            val file = File(context.filesDir, uri.lastPathSegment ?: "tempFile.jpg")
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            outputStream.close()
            inputStream.close()
            return file.absolutePath
        }
        return null
    }

    fun getFileName(context: Context, uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (columnIndex != -1) {
                        result = cursor.getString(columnIndex)
                    }
                }
            } finally {
                cursor?.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }

}
