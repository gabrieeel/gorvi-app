package com.gorvi.gorviapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import java.util.Locale

class PictogramActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private val TAG: String = PictogramActivity::class.java.name

    private lateinit var tts: TextToSpeech
    private var isSpeakerEnabled by mutableStateOf(true)
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tts = TextToSpeech(this, this).apply {
            setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    handler.post { isSpeakerEnabled = false }
                }

                override fun onDone(utteranceId: String?) {
                    handler.post { isSpeakerEnabled = true }
                }

                override fun onError(utteranceId: String?) {
                    handler.post { isSpeakerEnabled = true }
                }
            })
        }

        // Example usage
        val imageResId = intent.getIntExtra("IMAGE_RES_ID", 0) // Default value as 0
        val label = intent.getStringExtra("LABEL") ?: "" // Default value as empty string

        setContent {
            PictogramCard(imageResId = imageResId,
                label = label,
                isSpeakerEnabled = isSpeakerEnabled,
//                onSpeakClick = {
//                    if (::tts.isInitialized) {
//                        tts.speak(label, TextToSpeech.QUEUE_FLUSH, null, null)
//                        isSpeakerEnabled = false
//                        tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
//                            override fun onStart(utteranceId: String?) {
//                                // No action needed here for now
//                                Log.i(TAG, "onStart")
//                            }
//
//                            override fun onDone(utteranceId: String?) {
//                                Log.i(TAG, "onDone")
//                                isSpeakerEnabled = true
//                            }
//
//                            override fun onError(utteranceId: String?) {
//                                Log.i(TAG, "onError")
//                                isSpeakerEnabled = true
//                            }
//                        })
//                    }
//                }
                onSpeakClick = {
                    if (::tts.isInitialized && isSpeakerEnabled) {
                        tts.speak(label, TextToSpeech.QUEUE_FLUSH, null, "UniqueID")
                    }
                }
            )
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale("spa", "AR")
        } else {
            Log.e(TAG, "error TTS: $status")
        }
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }

    @Composable
    fun PictogramCard(imageResId: Int, label: String, isSpeakerEnabled: Boolean, onSpeakClick: () -> Unit) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = label, // Accessibility description
//                modifier = Modifier.border(1.dp, Color.Red)
            )
            Text(
                text = label,
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(top = 8.dp)
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_speaker), // Replace with your speaker icon resource
                contentDescription = "Speak",
                modifier = Modifier
                    .padding(16.dp)
                    .size(20.dp, 20.dp)
                    .clickable(enabled = isSpeakerEnabled, onClick = onSpeakClick)
            )
        }
    }
//
//    @Preview(showBackground = true)
//    @Composable
//    fun DefaultPreview() {
//        PictogramCard()
//    }

}
