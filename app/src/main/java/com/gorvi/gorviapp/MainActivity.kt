package com.gorvi.gorviapp

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items

import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.gorvi.gorviapp.data.Pictogram
import com.gorvi.gorviapp.ui.activities.admin.AddPictogramActivity
import com.gorvi.gorviapp.ui.activities.admin.DisplayPictogramsActivity
import com.gorvi.gorviapp.ui.activities.ShowPictogramActivity
import com.gorvi.gorviapp.ui.activities.ListPictogramsActivity
import com.gorvi.gorviapp.ui.viewmodel.PictogramViewModel
import com.gorvi.gorviapp.ui.viewmodel.PictogramViewModelFactory

class MainActivity : ComponentActivity() {

    private val REQUEST_CODE_ADD_PICTOGRAM = 1234

    private val wordsList = listOf("HOLA", "CHAU", "MAMA", "AUTO")

    private val viewModel: PictogramViewModel by viewModels {
        val app = application as GorviApp
        PictogramViewModelFactory(app.repository)
    }

    @Composable
    fun WordsList(words: List<String>) {
        LazyColumn {
            items(words) { word ->
                Text(text = word)
            }
        }
    }

    @Composable
    fun WordGrid(words: List<String>) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(5), // Change the number of columns as needed
            content = {
                items(words) { word ->
                    Text(word)
                }
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainScreen()
        }
    }

    @Composable
    fun MainScreen() {
        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally) {

            Button(onClick = {
                // Action to perform when button is clicked
                startActivity(Intent(this@MainActivity, NumbersActivity::class.java))
            }) {
                Text("NUMEROS")
            }


            Button(onClick = {
                // Action to perform when button is clicked
                startActivity(Intent(this@MainActivity, LettersActivity::class.java))
            }) {
                Text("LETRAS")
            }


//            Button(onClick = {
//                // Action to perform when button is clicked
//                startActivity(Intent(this@MainActivity, NumbersScrambleActivity::class.java))
//            }) {
//                Text("(NO) NUMBERS SCRAMBLE")
//            }

//            Button(onClick = {
//                // Action to perform when button is clicked
//                intent = Intent(this@MainActivity, PictogramActivity::class.java).apply {
//                    putExtra("IMAGE_RES_ID", R.drawable.auto)
//                    putExtra("LABEL", "AUTO")
//                }
//                startActivity(intent)
//            }) {
//                Text("AUTO")
//            }
//
//            Button(onClick = {
//                // Action to perform when button is clicked
//                intent = Intent(this@MainActivity, PictogramActivity::class.java).apply {
//                    putExtra("IMAGE_RES_ID", R.drawable.avion)
//                    putExtra("LABEL", "AVIÓN")
//                }
//                startActivity(intent)
//            }) {
//                Text("AVION")
//            }

            Button(onClick = {
                intent = Intent(this@MainActivity, AddPictogramActivity::class.java)
                startActivityForResult(intent, REQUEST_CODE_ADD_PICTOGRAM)
            }) {
                Text("ADD PICTOGRAM")
            }

            Button(onClick = {
                intent = Intent(this@MainActivity, DisplayPictogramsActivity::class.java)
                startActivity(intent)
            }) {
                Text("DISPLAY PICTOGRAMS")
            }

            Button(onClick = {
                startActivity(Intent(this@MainActivity, ListPictogramsActivity::class.java))
            }) {
                Text("PALABRAS")
            }

//            WordGrid(wordsList)

//            PictogramsScreen(viewModel)

            AppVersionScreen(getAppVersion())
        }

    }

    @Composable
    fun PictogramsScreen(viewModel: PictogramViewModel) {
        // Collect the Flow as state in Compose
        val pictograms = viewModel.allPictograms.collectAsState(initial = emptyList()).value

        Column {
            pictograms.forEach { pictogram ->
                PictogramButton(pictogram) {
                    // Handle button click
                    navigateToPictogramActivity(pictogram)
                }
            }
        }
    }

    @Composable
    fun PictogramButton(pictogram: Pictogram, onClick: () -> Unit) {
        Button(onClick = onClick) {
            Text(pictogram.label)
        }
    }

    // This function should be defined in MainActivity
    fun navigateToPictogramActivity(pictogram: Pictogram) {
        val intent = Intent(this@MainActivity, ShowPictogramActivity::class.java)
        intent.putExtra("PICTOGRAM_ID", pictogram.id)
        startActivity(intent)
    }

    private fun getAppVersion(): String {
        return try {
            packageManager.getPackageInfo(packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            "Unknown"
        }
    }

    @Composable
    fun AppVersionScreen(appVersion: String) {
        Text(text = "App Version: $appVersion")
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        var versionName = this.packageManager.getPackageInfo(this.packageName, 0).versionName
////
//        val txtVersion = findViewById<TextView>(R.id.txtVersion)
//        txtVersion.setText(versionName)
//
//        val button = findViewById<Button>(R.id.buttonOpenNumbers)
//        button.setOnClickListener {
//            val intent = Intent(this, NumbersActivity::class.java)
//            startActivity(intent)
//        }
//
//        val btnLetters = findViewById<Button>(R.id.buttonOpenLetters)
//        btnLetters.setOnClickListener {
//            val intent = Intent(this, LettersActivity::class.java)
//            startActivity(intent)
//        }
//
//        val btnWords = findViewById<Button>(R.id.buttonOpenWords)
//        btnWords.setOnClickListener {
//            val intent = Intent(this, WordsActivity::class.java)
//            startActivity(intent)
//        }
//
//    }

    @Composable
    fun MyApp(content: @Composable () -> Unit) {
        MaterialTheme {
            Surface(modifier = Modifier.fillMaxSize()) {
                content()
            }
        }
    }

    @Composable
    fun MyScreenContent() {
        Box(modifier = Modifier.fillMaxSize()) {
            BasicText(text = "Hello, Jetpack Compose!")
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        MyApp {
            MyScreenContent()
        }
    }

    // Override onActivityResult
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_ADD_PICTOGRAM && resultCode == RESULT_OK) {
            Toast.makeText(this, "Pictogram saved successfully", Toast.LENGTH_SHORT).show()
        }
    }
}
