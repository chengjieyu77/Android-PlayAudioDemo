package com.example.playaudiodemo.media.exoplayer

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import androidx.media3.common.MediaItem
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.source.ConcatenatingMediaSource
import androidx.media3.exoplayer.source.ConcatenatingMediaSource2
import androidx.media3.exoplayer.source.ProgressiveMediaSource


import com.example.playaudiodemo.data.repository.AudioRepository
import javax.inject.Inject

class MediaSource @Inject constructor(private val repository:AudioRepository){
    private val onReadyListeners:MutableList<OnReadyListener> = mutableListOf()

    var audioMediaMetaData:List<MediaMetadataCompat> = emptyList()
    private var state:AudioSourceState = AudioSourceState.STATE_CREATED
        set(value) {
            if (value == AudioSourceState.STATE_CREATED
                || value == AudioSourceState.STATE_ERROR){
                synchronized(onReadyListeners){ //synchronized
                    field = state
                    onReadyListeners.forEach { listener:OnReadyListener->
                        listener.invoke(isReady)
                    }
                }
            }else{
                field = value
            }
        }

    suspend fun load() {
        state = AudioSourceState.STATE_INITIALIZING
        val data = repository.getAudioData()
        audioMediaMetaData = data.map {audio->
            MediaMetadataCompat.Builder()
                .putString(
                    MediaMetadataCompat.METADATA_KEY_MEDIA_ID,
                    audio.id.toString()
                )
                .putString(
                    MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST,
                    audio.artist
                )
                .putString(
                    MediaMetadataCompat.METADATA_KEY_MEDIA_URI,
                    audio.uri.toString()
                )
                .putString(
                    MediaMetadataCompat.METADATA_KEY_TITLE,
                    audio.title
                )
                .putString(
                    MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE,
                    audio.displayName
                ).putLong(
                    MediaMetadataCompat.METADATA_KEY_DURATION,
                    audio.duration.toLong()
                )
                .build()
        }
        state = AudioSourceState.STATE_INITIALIZED
    }

    //suitable for playing, used by exoplayer
    fun asMediaSource(dataSource:CacheDataSource.Factory):
            ConcatenatingMediaSource {
        val concatenatingMediaSource = ConcatenatingMediaSource()

        audioMediaMetaData.forEach { mediaMetadataCompat ->
            val mediaItem = MediaItem.fromUri(
                mediaMetadataCompat.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI)
            )

            val mediaSource = ProgressiveMediaSource
                .Factory(dataSource)
                .createMediaSource(mediaItem)

            concatenatingMediaSource.addMediaSource(mediaSource)

        }
        return concatenatingMediaSource
    }


    //used by media browse service to return data to the client
    fun asMediaItem() = audioMediaMetaData.map { metaData->
        val description = MediaDescriptionCompat.Builder()
            .setTitle(metaData.description.title)
            .setMediaId(metaData.description.mediaId)
            .setSubtitle(metaData.description.subtitle)
            .setMediaUri(metaData.description.mediaUri)
            .build()
        MediaBrowserCompat.MediaItem(description, MediaBrowserCompat.MediaItem.FLAG_PLAYABLE)
    }.toMutableList()


    fun refresh(){
        onReadyListeners.clear()
        state = AudioSourceState.STATE_CREATED
    }




    fun whenReady(listener:OnReadyListener):Boolean{
        return if (state == AudioSourceState.STATE_CREATED ||
            state == AudioSourceState.STATE_INITIALIZING){
            onReadyListeners += listener
            false
        }else{
            listener.invoke(isReady)
            true
        }
    }

    private val isReady:Boolean
        get() = state == AudioSourceState.STATE_INITIALIZED
}
enum class AudioSourceState{
    STATE_CREATED,
    STATE_INITIALIZING,
    STATE_INITIALIZED,
    STATE_ERROR
}

//typelias means a way to give an existing type a new name
//so it provides an alternative naming to your existing type to reflect
//a more readable code. So we are just going to use this lambda function here
//to communicate to our media player service compact here and in order to do that
//we have to create this function here so we can just add this typealias here to give the
//name to this boolean function so that when we just pass this onreadylistener we can understand
//we are just going to use this onReadListener
typealias OnReadyListener = (Boolean) -> Unit