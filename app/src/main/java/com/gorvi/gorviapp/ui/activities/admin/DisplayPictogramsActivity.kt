package com.gorvi.gorviapp.ui.activities.admin

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gorvi.gorviapp.data.Pictogram
import com.gorvi.gorviapp.ui.viewmodel.PictogramViewModel
import androidx.compose.runtime.collectAsState
import com.gorvi.gorviapp.GorviApp
import com.gorvi.gorviapp.ui.viewmodel.PictogramViewModelFactory
import java.io.File
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter

class DisplayPictogramsActivity : AppCompatActivity() {

    private val viewModel: PictogramViewModel by viewModels {
        // Initialize your repository here and pass it to the factory
        val app = application as GorviApp
        PictogramViewModelFactory(app.repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PictogramsScreen(viewModel)
        }
    }

    @Composable
    fun PictogramsScreen(viewModel: PictogramViewModel) {
        // Observe the LiveData from the ViewModel
        val pictograms = viewModel.allPictograms.collectAsState(initial = emptyList())

        PictogramList(pictograms = pictograms.value)
    }

    @Composable
    fun PictogramList(pictograms: List<Pictogram>) {
        LazyColumn {
            items(pictograms) { pictogram ->
                PictogramItem(pictogram)
            }
        }
    }

    @Composable
    fun PictogramItem(pictogram: Pictogram) {
        Log.d("PictogramItem", "Loading image from path: ${pictogram.imagePath}")

        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = pictogram.label + " " + pictogram.imagePath, style = MaterialTheme.typography.headlineLarge)

            val imagePainter = rememberAsyncImagePainter(File(pictogram.imagePath))
            Image(
                painter = imagePainter,
                contentDescription = null, // Provide a description for accessibility
                modifier = Modifier.padding(top = 8.dp).size(100.dp),
                contentScale = ContentScale.Crop // Or choose another appropriate ContentScale
            )

//            val imagePainter = rememberImagePainter(
//                data = File(pictogram.imagePath),
//                builder = {
//                    crossfade(true)
//                    error(R.drawable.error_image) // Replace with an actual error drawable
//                }
//            )

            Button(onClick = { viewModel.deletePictogram(pictogram) }) {
                Text("Delete")
            }
        }
    }

}
