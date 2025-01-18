package com.soundnote.playground

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.soundnote.R
import com.soundnote.databinding.ActivityPlaygroundBinding
import com.soundnote.databinding.ViewNoteRowBinding
import com.soundnote.playground.internal.SingleNote
import com.soundnote.playground.internal.State

class PlaygroundActivity: AppCompatActivity() {

    lateinit var playgroundBinding: ActivityPlaygroundBinding
    lateinit var player: ExoPlayer
    lateinit var handler: Handler
    var timeStamp: Long = 0L
    var state = State.START
    var startTime = 0L
    var endTime = 0L
    var notesList = mutableListOf<SingleNote>()

    @OptIn(UnstableApi::class)
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

            Log.d("SeekFinder CurrentTime : + $state + :" ,  player.currentPosition.toString())

            when(state){
                State.START -> {
                    startTime = player.currentPosition
                    state = State.IN_LOOP
                }
                State.IN_LOOP -> {
                    endTime = player.currentPosition
                    state = State.START
                    createSingleNoteAndPushToList(startTime, endTime)
                    populateView(notesList.size - 1)
                }
                State.END -> {
                    state = State.START
                    playNote(startTime, endTime)
                }
            }

        }

//        val mediaItem = MediaItem.fromUri("https://download.samplelib.com/mp3/sample-12s.mp3")
        val mediaItem = MediaItem.fromUri("http://commondatastorage.googleapis.com/codeskulptor-demos/DDR_assets/Sevish_-__nbsp_.mp3")
        player.setMediaItem(mediaItem)
        player.prepare()
        playgroundBinding.epMusic.controllerShowTimeoutMs = 0
        playgroundBinding.epMusic.controllerHideOnTouch = false

        handler = Handler(Looper.getMainLooper())

        Log.d("SeekFinder", player.currentPosition.toString())
        player.play()
    }

    fun populateView(index: Int) {
        val note = notesList.get(index)
        val view: ViewNoteRowBinding = ViewNoteRowBinding.inflate(layoutInflater)
        view.ttStartTime.text = ((note._startTime * 1F)/1000F).toString()
        view.ttEndTime.text = ((note._endTime * 1F)/1000F).toString()
        view.root.setOnClickListener({
            playNote(note._startTime, note._endTime)
        })
        playgroundBinding.llNotesList.addView(view.root)
    }

    fun playNote(startTime: Long, endTime: Long) {
        player.seekTo(startTime)
        player.play()
        pauseAfterSpecifiedTimeDelay(endTime - startTime)
    }

    private fun createSingleNoteAndPushToList(startTime: kotlin.Long, endTime: Long) {
        val newSingleNote = SingleNote(startTime, endTime)
        notesList.add(newSingleNote)
    }

    fun pauseAfterOneSecondDelay(){
        handler.postDelayed({
            Log.d("SeekFinder CurrentTime : ",  player.currentPosition.toString())
            player.pause()
        }, 1000)
    }

    fun pauseAfterSpecifiedTimeDelay(timeInMillis: Long){
        handler.postDelayed({
            Log.d("SeekFinder CurrentTime : ",  player.currentPosition.toString())
            player.pause()
        }, timeInMillis)
    }
}