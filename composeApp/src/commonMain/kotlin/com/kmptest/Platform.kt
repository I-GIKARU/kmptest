package com.kmptest

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform