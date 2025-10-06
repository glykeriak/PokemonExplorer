package com.example.pokmon_explorer.data

fun extractedID(url: String): String = url.trimEnd('/').split("/").last()
