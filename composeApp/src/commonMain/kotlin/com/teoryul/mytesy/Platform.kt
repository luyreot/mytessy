package com.teoryul.mytesy

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform