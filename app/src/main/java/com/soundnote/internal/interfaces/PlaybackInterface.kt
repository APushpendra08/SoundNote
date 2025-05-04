package com.soundnote.internal.interfaces

interface PlaybackInterface {

    fun play()
    fun pause()
    fun seekTo(ms: Long)
    fun playNote(startTime: Long, endTime: Long)
    fun pauseAfterSpecifiedTimeDelay(ms: Long)
    fun playbackCurrentPosition(): Long
//    pauseAfterSpecifiedTimeDelay
}