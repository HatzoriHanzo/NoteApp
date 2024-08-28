package com.example.noteapp.presentation.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Long.toLocalDateTime(): LocalDateTime {
    return Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDateTime()
}

fun LocalDateTime.formatToddMMyyyyHHmm(): String {
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy - HH:mm")
    return this.format(formatter)
}
fun LocalDateTime.toEpochMilli(): Long {
    return this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}