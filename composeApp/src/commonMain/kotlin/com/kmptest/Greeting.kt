package com.kmptest

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Good evening, ${platform.name}!"
    }
}