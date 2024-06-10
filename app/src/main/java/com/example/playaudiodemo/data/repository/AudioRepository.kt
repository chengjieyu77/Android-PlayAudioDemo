package com.example.playaudiodemo.data.repository

import com.example.playaudiodemo.data.ContentResolverHelper
import com.example.playaudiodemo.data.model.Audio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AudioRepository @Inject
constructor(private val contentResolverHelper: ContentResolverHelper) {
    suspend fun getAudioData():List<Audio> = withContext(Dispatchers.IO){
        contentResolverHelper.getAudioData()//no need to worry about ui thread to be overworked
    }
}