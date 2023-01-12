package com.example.kmmtestapplication

interface Logger {
    fun e(message: String)
    fun e(exception: Throwable)
}

object Log : Logger {

    private val PERFIX = "LOG: "

    override fun e(message: String) {
        println("$PERFIX $message")
    }

    override fun e(exception: Throwable) {
        println("$PERFIX ${exception.stackTraceToString()}")
    }

}