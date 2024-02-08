package com.gorvi.gorviapp.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.dp
import com.gorvi.gorviapp.GorviApp
import com.gorvi.gorviapp.data.Pictogram
import com.gorvi.gorviapp.ui.viewmodel.PictogramViewModel
import com.gorvi.gorviapp.ui.viewmodel.PictogramViewModelFactory
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items

class ListPictogramsActivity : AppCompatActivity() {

    private val viewModel: PictogramViewModel by viewModels {
        val app = application as GorviApp
        PictogramViewModelFactory(app.repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PictogramLabelsGrid(viewModel)
        }
    }

    @Composable
    fun PictogramLabelsGrid(viewModel: PictogramViewModel) {
        // Collect the Flow as state in Compose
        val pictograms = viewModel.allPictograms.collectAsState(initial = emptyList()).value

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            content = {
                items(pictograms) { pictogram ->
                    PictogramButton(pictogram) {
                        // Handle button click
                        navigateToPictogramActivity(pictogram)
                    }
                }
            }
        )
    }

    @Composable
    fun PictogramButton(pictogram: Pictogram, onClick: () -> Unit) {
        Button(onClick = onClick) {
            Text(pictogram.label)
        }
    }

    fun navigateToPictogramActivity(pictogram: Pictogram) {
        val intent = Intent(this@ListPictogramsActivity, ShowPictogramActivity::class.java)
        intent.putExtra("PICTOGRAM_ID", pictogram.id)
        startActivity(intent)
    }
}