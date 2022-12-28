package com.example.kmmtestapplication

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform