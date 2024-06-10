package com.example.playaudiodemo.data

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.WorkerThread
import com.example.playaudiodemo.data.model.Audio
import dagger.hilt.android.qualifiers.ApplicationContext

import javax.inject.Inject

private const val TAG = "ContentResolverHelper"

class ContentResolverHelper @Inject constructor(@ApplicationContext val context:Context) {
    //the data going to be returned when we query this database,
    //and it represents in a tabular format with the rows and columns
    private var mCursor:Cursor?=null
    private val projection:Array<String> = arrayOf(
            MediaStore.Audio.AudioColumns.DISPLAY_NAME,
        MediaStore.Audio.AudioColumns._ID,
        MediaStore.Audio.AudioColumns.ARTIST,
        MediaStore.Audio.AudioColumns.DATA,
        MediaStore.Audio.AudioColumns.DURATION,
        MediaStore.Audio.AudioColumns.TITLE,
        )

    private var selectionClause:String? = "${MediaStore.Audio.AudioColumns.IS_MUSIC} = ?"
    private var selectionArg = arrayOf("1")
    private val sortOrder = "${MediaStore.Audio.AudioColumns.DISPLAY_NAME} ASC"

    @WorkerThread
    fun getAudioData():List<Audio>{
        return getCursorData()
    }

    private fun getCursorData():MutableList<Audio>{
        val audioList = mutableListOf<Audio>()

        mCursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,//column we want to select
            selectionClause,
            selectionArg,
            sortOrder
        )

        mCursor?.use {cursor->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns._ID)
            val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DISPLAY_NAME)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATA)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DURATION)

            cursor.apply {
                if (count == 0){
                    Log.e(TAG,"getCursorData:Cursor is empty")
                }else{
                    while (cursor.moveToNext()){
                        val displayName = getString(displayNameColumn)
                        val id = getLong(idColumn)
                        val artist = getString(artistColumn)
                        val data = getString(dataColumn)
                        val duration = getInt(durationColumn)
                        val title = getString(titleColumn)
                        val uri = ContentUris.withAppendedId(
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                            id
                        )

                        audioList += Audio(
                            uri,displayName,id,artist,data,duration,title
                        )
                    }
                }
            }
        }
        return  audioList
    }


}