package com.soundnote.internal.players

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.media3.exoplayer.ExoPlayer
import com.soundnote.internal.interfaces.PlaybackInterface

class ExoPlayerPlayer: PlaybackInterface {

    lateinit var _exoplayer: ExoPlayer
    lateinit var _handler: Handler

    constructor(player: ExoPlayer, context: Context){
        _exoplayer = player
        _handler = Handler(Looper.getMainLooper())
    }

    override fun play() {
        Log.d("diff", _exoplayer.currentPosition.toString())
        _exoplayer.play()
    }

    override fun pause() {
        _exoplayer.pause()
    }

    override fun seekTo(ms: Long) {
        _exoplayer.seekTo(ms)
    }

    override fun playNote(startTime: Long, endTime: Long) {
        seekTo(startTime)
        play()
        removeOlderPauses()
        Log.d("Diff", endTime.toString() + " " + startTime.toString() + " " + (endTime - startTime).toString())
        pauseAfterSpecifiedTimeDelay(endTime - startTime)
    }

    override fun pauseAfterSpecifiedTimeDelay(timeInMillis: Long){
        Log.d("SeekFinder CurrentTime : handler before posting",  timeInMillis.toString())
        _handler.postDelayed({
            Log.d("SeekFinder CurrentTime : handler after posting",  _exoplayer.currentPosition.toString())
            _exoplayer.pause()
        }, timeInMillis)
    }

    internal fun removeOlderPauses() {
        _handler.removeCallbacksAndMessages(null)
    }

    override fun playbackCurrentPosition(): Long {
        return _exoplayer.currentPosition
    }
}