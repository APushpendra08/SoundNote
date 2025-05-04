package com.soundnote.playground

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.soundnote.R
import com.soundnote.databinding.ActivityExoplayerPlaybackBinding
import com.soundnote.databinding.ViewNoteRowBinding
import com.soundnote.internal.notes.ExoPlayerNote
import com.soundnote.internal.notes.SingleNote
import com.soundnote.internal.players.ExoPlayerPlayer

class ExoplayerPlaybackActivity : AppCompatActivity() {

    lateinit var binding: ActivityExoplayerPlaybackBinding
    lateinit var _player: ExoPlayer
    lateinit var exoPlayerNote: ExoPlayerNote

    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityExoplayerPlaybackBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        _player = ExoPlayer.Builder(this).build()
        val mediaItem = MediaItem.fromUri("http://commondatastorage.googleapis.com/codeskulptor-demos/DDR_assets/Sevish_-__nbsp_.mp3")
        _player.setMediaItem(mediaItem)
        _player.prepare()
        exoPlayerNote = ExoPlayerNote(this, _player)
        binding.epMusic.player = _player
        binding.btnTimeMap.setOnClickListener{
            val timeNote = exoPlayerNote.startNoting()
            timeNote?.let {
                populateView(timeNote)
            }
        }

        binding.epMusic.controllerShowTimeoutMs = 0
        binding.epMusic.controllerHideOnTouch = false

    }

    fun populateView(note: SingleNote) {
//        val note = notesList.get(index)
        val view: ViewNoteRowBinding = ViewNoteRowBinding.inflate(layoutInflater)
        view.ttStartTime.text = ((note._startTime * 1F)/1000F).toString()
        view.ttEndTime.text = ((note._endTime * 1F)/1000F).toString()
        view.root.setOnClickListener({
            exoPlayerNote.playNote(note._startTime, note._endTime)
        })
        binding.llNotesList.addView(view.root)
    }
}