package com.example.kmmtestapplication.greeting

import com.example.kmmtestapplication.greeting.Platform

class AndroidPlatform : Platform {
    override val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()