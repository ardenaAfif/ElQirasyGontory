package com.gontory.elqirasygontory.utils

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class AudioService : Service() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
//        mediaPlayer =
    }
}