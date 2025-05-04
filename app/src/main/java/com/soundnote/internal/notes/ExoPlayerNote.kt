package com.soundnote.internal.notes

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import com.soundnote.internal.players.ExoPlayerPlayer

class ExoPlayerNote {

    lateinit var player: ExoPlayerPlayer
    lateinit var state: PlayerState
    var startTime: Long = 0L
    var endTime: Long = 0L
    val notes = mutableListOf<SingleNote>()

    constructor(context: Context, exoPlayer: ExoPlayer){
        player = ExoPlayerPlayer(exoPlayer, context)
        state = PlayerState.START
    }

    fun startNoting() : SingleNote? {
        return when(state){
            PlayerState.START -> {
                startTime = player.playbackCurrentPosition()
                state = PlayerState.IN_LOOP
                return null
            }
            PlayerState.IN_LOOP -> {
                endTime = player.playbackCurrentPosition()
                state = PlayerState.START
                createSingleNoteAndPushToList(startTime, endTime)
                return notes.last()
            }
            PlayerState.END -> {
                state = PlayerState.START
                return null
            }
        }
    }

    fun playNote(startTime: Long, endTime: Long) {
        player.playNote(startTime, endTime)
    }

    fun stopNoting(): SingleNote? {
        return when(state){
            PlayerState.START -> {
                startTime = player.playbackCurrentPosition()
                state = PlayerState.IN_LOOP
                return null
            }
            PlayerState.IN_LOOP -> {
                endTime = player.playbackCurrentPosition()
                state = PlayerState.START
                createSingleNoteAndPushToList(startTime, endTime)
                return notes.last()
            }
            PlayerState.END -> {
                state = PlayerState.START
                return null
            }
        }
    }

    private fun createSingleNoteAndPushToList(startTime: kotlin.Long, endTime: Long) {
        val newSingleNote = SingleNote(startTime, endTime)
        notes.add(newSingleNote)
    }


}