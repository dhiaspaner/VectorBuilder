package org.path.builder.project

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform