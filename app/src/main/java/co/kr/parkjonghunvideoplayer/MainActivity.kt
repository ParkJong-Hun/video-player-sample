package co.kr.parkjonghunvideoplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import co.kr.parkjonghunvideoplayer.ui.theme.VideoPlayerSampleTheme

class MainActivity : ComponentActivity() {
    private lateinit var player: Player

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        player = ExoPlayer.Builder(this).build()

        setContent {
            VideoPlayerSampleTheme {
                val mediaSource = remember(SAMPLE_HLS_URL) {
                    MediaItem.fromUri(SAMPLE_HLS_URL)
                }

                LaunchedEffect(mediaSource) {
                    player.setMediaItem(mediaSource)
                    player.prepare()
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    VideoScreen(
                        player = player,
                        modifier = Modifier.padding(innerPadding),
                    )
                }

                DisposableEffect(Unit) {
                    onDispose {
                        player.release()
                    }
                }
            }
        }
    }

    companion object {
        // ref: https://ottverse.com/free-hls-m3u8-test-urls/
        private const val SAMPLE_HLS_URL =
            "https://demo.unified-streaming.com/k8s/features/stable/video/tears-of-steel/tears-of-steel.ism/.m3u8"
    }
}

@Composable
fun VideoScreen(
    player: Player,
    modifier: Modifier = Modifier,
) {
    AndroidView(
        factory = { context ->
            PlayerView(context).apply {
                this.player = player
            }
        },
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun VideoScreenPreview() {
    VideoPlayerSampleTheme {
        VideoScreen(
            player = ExoPlayer.Builder(LocalContext.current).build(),
            modifier = Modifier.fillMaxSize(),
        )
    }
}