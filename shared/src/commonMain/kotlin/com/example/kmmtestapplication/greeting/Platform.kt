package com.example.kmmtestapplication.greeting

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform