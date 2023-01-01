package com.temp.ggtest

import coil.Coil
import coil.ImageLoader
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
open class MyApplication : BaseApplication(){
    @Inject
    lateinit var imageLoader: ImageLoader
    override fun onCreate() {
        super.onCreate()
        Coil.setImageLoader(imageLoader)
    }
}