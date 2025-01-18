package com.soundnote.playground

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.soundnote.R
import com.soundnote.databinding.ActivityPlaygroundBinding

class PlaygroundActivity: AppCompatActivity() {

    lateinit var playgroundBinding: ActivityPlaygroundBinding
    lateinit var player: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playgroundBinding = ActivityPlaygroundBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(playgroundBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        player = ExoPlayer.Builder(this).build()

        playgroundBinding.epMusic.player = player
        playgroundBinding.btnTimeMap.setOnClickListener{
            Log.d("SeekFinder CurrentTime : ",  player.currentPosition.toString())
        }

//        val mediaItem = MediaItem.fromUri("https://download.samplelib.com/mp3/sample-12s.mp3")
        val mediaItem = MediaItem.fromUri("http://commondatastorage.googleapis.com/codeskulptor-demos/DDR_assets/Sevish_-__nbsp_.mp3")
        player.setMediaItem(mediaItem)
        player.prepare()

        Log.d("SeekFinder", player.currentPosition.toString())
//        player.play()
    }
}