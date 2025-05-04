package com.soundnote.internal.notes

class SingleNote(startTime: Long = 0L, endTime: Long = 0L) {
    val _startTime: Long
    val _endTime: Long
    val duration: Long

    init {
        _startTime = startTime
        _endTime = endTime
        duration = (endTime - startTime)
    }
}