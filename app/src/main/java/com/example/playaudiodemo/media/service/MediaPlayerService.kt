package com.example.playaudiodemo.media.service

import android.media.browse.MediaBrowser
import android.os.Bundle
import android.service.media.MediaBrowserService
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.media.MediaBrowserServiceCompat
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.CacheDataSource.Factory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import com.example.playaudiodemo.media.constants.K
import com.example.playaudiodemo.media.exoplayer.MediaSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Inject

@AndroidEntryPoint
class MediaPlayerService: MediaBrowserServiceCompat() {

   @Inject
    lateinit var dataSourceFactory: CacheDataSource.Factory

    @Inject
    lateinit var exoPlayer:ExoPlayer

    @Inject
    lateinit var mediaSource: MediaSource

    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private lateinit var mediaSession: MediaSessionCompat

    //private lateinit var mediaSessionConnector:MediaSessionConnector

     override fun onGetRoot(
         clientPackageName: String,
         clientUid: Int,
         rootHints: Bundle?
     ): BrowserRoot {
         return BrowserRoot(K.MEDIA_ROOT_ID,null)
     }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {

    }

 }