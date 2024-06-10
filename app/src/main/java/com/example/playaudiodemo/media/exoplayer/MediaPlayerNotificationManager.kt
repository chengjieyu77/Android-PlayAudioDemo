package com.example.playaudiodemo.media.exoplayer

import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat.Token
import androidx.media3.common.Player
import androidx.media3.ui.PlayerNotificationManager
import androidx.media3.ui.PlayerNotificationManager.NotificationListener
import com.example.playaudiodemo.R
import com.example.playaudiodemo.media.constants.K

internal class MediaPlayerNotificationManager(
    context: Context,
    sessionToken: Token,
    notificationListener: NotificationListener
) {
    private val notificationManager:PlayerNotificationManager

    init {

        val mediaController = MediaControllerCompat(context,sessionToken)

        val builder = PlayerNotificationManager.Builder(
            context,
            K.PLAYBACK_NOTIFICATION_ID,
            K.PLAYBACK_NOTIFICATION_CHANNEL_ID
        )

        with(builder){
            setMediaDescriptionAdapter(DescriptionAdapter(mediaController))
            setNotificationListener(notificationListener)
            setChannelNameResourceId(R.string.notification_channel)
            setChannelDescriptionResourceId(R.string.notification_channel_description)
        }

        notificationManager = builder.build()

        with(notificationManager){
            setMediaSessionToken(sessionToken)
            setSmallIcon()
        }

    }

    inner class DescriptionAdapter(private val controller:MediaControllerCompat):
        PlayerNotificationManager.MediaDescriptionAdapter{

        override fun getCurrentContentTitle(player: Player): CharSequence =
           controller.metadata.description.title.toString()


        override fun createCurrentContentIntent(player: Player): PendingIntent? =
           controller.sessionActivity


        override fun getCurrentContentText(player: Player): CharSequence? =
            controller.metadata.description.subtitle


        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {
            return null
        }

    }
}

