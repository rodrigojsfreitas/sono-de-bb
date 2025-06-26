package com.example.sonodebb

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Cores
val screenBgColor = Color(0xFFF7F7F7)
val titleColor = Color(0xFF5A6774)
val textAreaColor = Color(0xFFBCCAD6)
val iconAreaColor = Color(0xFFD3DDE5)
val buttonContentColor = Color(0xFFFFFFFF)

// Classe de dados para os sons
data class Sound(val name: String, val resourceId: Int)

@Preview
@Composable
private fun PreviewSons() {
    SonsScreen()
}
@Composable
fun SonsScreen(padding: PaddingValues = PaddingValues(0.dp)) {
    val context = LocalContext.current

    // Lista com os sons e seus recursos
    val soundItems = listOf(
        Sound("Riacho", R.raw.riacho),
        Sound("Vento", R.raw.vento),
        Sound("Chuva", R.raw.chuva)
    )

    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var currentlyPlaying by remember { mutableStateOf<String?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize().padding(padding),
        color = screenBgColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Sons",
                fontSize = 36.sp,
                fontWeight = FontWeight.Normal,
                color = titleColor
            )

            Spacer(modifier = Modifier.height(48.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                soundItems.forEach { sound ->
                    SoundButton(
                        text = sound.name,
                        isPlaying = currentlyPlaying == sound.name,
                        onClick = {
                            if (currentlyPlaying == sound.name) {
                                // Parar o som atual
                                mediaPlayer?.stop()
                                mediaPlayer?.release()
                                mediaPlayer = null
                                currentlyPlaying = null
                            } else {
                                // Parar som anterior se estiver tocando
                                mediaPlayer?.stop()
                                mediaPlayer?.release()

                                // Iniciar novo som em loop
                                mediaPlayer = MediaPlayer.create(context, sound.resourceId).apply {
                                    isLooping = true  // Configura para loop infinito
                                    start()
                                }
                                currentlyPlaying = sound.name
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SoundButton(text: String, isPlaying: Boolean, onClick: () -> Unit) {
    val cornerRadius = 16.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clip(RoundedCornerShape(cornerRadius))
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .background(iconAreaColor)
                .fillMaxHeight()
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = if(isPlaying) painterResource(R.drawable.pause) else painterResource(R.drawable.play),
                contentDescription = if(isPlaying) "Parar som de $text" else "Tocar som de $text",
                tint = buttonContentColor,
                modifier = Modifier.size(30.dp)
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .background(textAreaColor)
                .fillMaxHeight()
                .padding(vertical = 20.dp, horizontal = 24.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = text,
                color = buttonContentColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}