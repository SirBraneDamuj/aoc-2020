package com.zpthacker.aoc20.day24

import com.zpthacker.aoc20.getInputLines

fun main() {
    val lines = getInputLines("day24")
    val grid = mutableMapOf<Pair<Int, Int>, TileColor>()

    lines.forEach { line ->
        var x = 0
        var y = 0
        var rest = line
        var next: String
        do {
            next = rest.takeUntil { it == 'e' || it == 'w' }
            rest = rest.drop(next.length)
            if (next.length == 2) {
                val (vert, hori) = next.toCharArray()
                x += when (hori) {
                    'e' -> 1
                    'w' -> -1
                    else -> error("oh dear")
                }
                y += when (vert) {
                    'n' -> 1
                    's' -> -1
                    else -> error("oh dear")
                }
            } else if (next.length == 1) {
                x += when (next) {
                    "e" -> 2
                    "w" -> -2
                    else -> error("oh dear")
                }
            }
        }
        while (rest != "")
        grid.computeIfAbsent(Pair(x, y)) { TileColor.WHITE }
        grid.computeIfPresent(Pair(x, y)) { _, color -> color.flip() }
    }
    println(lines.count())
    println(grid.count())
    println(grid.values.count { it == TileColor.BLACK })
}

fun String.takeUntil(predicate: (Char) -> Boolean): String {
    var s = ""
    for (c in this) {
        s += c
        if (predicate(c)) return s
    }
    return s
}

enum class TileColor {
    WHITE,
    BLACK;

    fun flip() = when (this) {
        WHITE -> BLACK
        BLACK -> WHITE
    }
}