package com.gorvi.gorviapp

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.*

class NumbersActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private lateinit var textToSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize TextToSpeech
        textToSpeech = TextToSpeech(this, this)

        setContent {
            NumbersGrid { number ->
                speakNumber(number)
            }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech.language = Locale("spa", "AR")
        }
    }

    private fun speakNumber(number: Int) {
        textToSpeech.speak(number.toString(), TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onDestroy() {
        if (::textToSpeech.isInitialized) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
        super.onDestroy()
    }
}

@Composable
fun NumbersGrid(onNumberClick: (Int) -> Unit) {
    val numbers = (1..30).toList()

    LazyVerticalGrid(
        columns = GridCells.Fixed(5),
        contentPadding = PaddingValues(5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(numbers) { number ->
            Button(onClick = { onNumberClick(number) },
                shape = RoundedCornerShape(percent = 10),
//                shape = CircleShape,
//                modifier = Modifier.clip(RoundedCornerShape(10.dp, 10.dp, 0.dp, 0.dp)),
//                contentPadding = PaddingValues(
//                    start = 1.dp,
//                    top = 8.dp,
//                    end = 1.dp,
//                    bottom = 8.dp
//                ),
                modifier = Modifier.wrapContentSize(),
                contentPadding = PaddingValues(4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(16,9,53)),
                border = BorderStroke(5.dp, Color(0,188,212))
            ) {
                Text(text = number.toString())
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NumbersGrid {    }
}