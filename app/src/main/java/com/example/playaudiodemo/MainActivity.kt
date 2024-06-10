package com.example.playaudiodemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.playaudiodemo.ui.theme.PlayAudioDemoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlayAudioDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val colorStops = arrayOf(
        0.0f to Color.Yellow,
        0.2f to Color.Red,
        1f to Color.Blue
    )
    val brush = Brush.horizontalGradient(colorStops = colorStops)
    Box(
        modifier = Modifier
            .requiredSize(200.dp)
            .drawBehind {
                drawRect(brush = brush) // will allocate a shader to occupy the 200 x 200 dp drawing area
                inset(10f) {
                    /* Will allocate a shader to occupy the 180 x 180 dp drawing area as the
                     inset scope reduces the drawing  area by 10 pixels on the left, top, right,
                    bottom sides */
                    drawRect(brush = brush)
                    inset(5f) {
                        /* will allocate a shader to occupy the 170 x 170 dp drawing area as the
                         inset scope reduces the  drawing area by 5 pixels on the left, top,
                         right, bottom sides */
                        drawRect(brush = brush)
                    }
                }
            }
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PlayAudioDemoTheme {
        Greeting("Android")
    }
}