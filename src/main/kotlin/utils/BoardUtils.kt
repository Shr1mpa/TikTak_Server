package com.example.utils

fun initEmptyBoard(): MutableMap<String, String?> {
    val board = mutableMapOf<String, String?>()
    val rows = listOf("1", "2", "3")
    val cols = listOf("A", "B", "C")
    for (r in rows) {
        for (c in cols) {
            board["$c$r"] = null
        }
    }
    return board
}