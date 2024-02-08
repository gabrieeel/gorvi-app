package com.gorvi.gorviapp.ui.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.gorvi.gorviapp.GorviApp
import com.gorvi.gorviapp.R
import com.gorvi.gorviapp.ui.viewmodel.PictogramViewModel
import com.gorvi.gorviapp.ui.viewmodel.PictogramViewModelFactory
import java.io.File
import java.util.Locale

class ShowPictogramActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private val TAG = ShowPictogramActivity::class.qualifiedName

    private lateinit var tts: TextToSpeech
    private var isSpeakerEnabled by mutableStateOf(true)
    private val handler = Handler(Looper.getMainLooper())

    private val viewModel: PictogramViewModel by viewModels {
        val app = application as GorviApp
        PictogramViewModelFactory(app.repository)
    }

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

        val pictogramId = intent.getIntExtra("PICTOGRAM_ID", -1)

        if (pictogramId != -1) {
            setContent {
                PictogramCard(pictogramId, viewModel,
                    isSpeakerEnabled = isSpeakerEnabled
                )
            }
        } else {
            setContent {
                Text("Error")
            }
        }
    }

    @Composable
    fun PictogramCard(pictogramId: Int, viewModel: PictogramViewModel, isSpeakerEnabled: Boolean) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
            val pictogram = viewModel.getPictogramById(pictogramId).collectAsState(initial = null).value
            pictogram?.let {
                val onSpeakClick = {
                    if (::tts.isInitialized && isSpeakerEnabled) {
                        tts.speak(pictogram.label, TextToSpeech.QUEUE_FLUSH, null, "UniqueID")
                    }
                }

                val imagePainter = rememberAsyncImagePainter(File(pictogram.imagePath))
                Image(
                    painter = imagePainter,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 8.dp),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = pictogram.label,
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_speaker),
                    contentDescription = "Speak",
                    modifier = Modifier
                        .padding(16.dp)
                        .size(20.dp, 20.dp)
                        .clickable(enabled = isSpeakerEnabled, onClick = onSpeakClick)
                )

                Icon(
                    painter = painterResource(id = R.drawable.ic_round_arrow_back_black_48),
                    contentDescription = "Back",
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable(onClick = { finish() } )
                )

            }
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

}