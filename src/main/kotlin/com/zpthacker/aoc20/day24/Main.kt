package com.zpthacker.aoc20.day24

import com.zpthacker.aoc20.getInputLines

fun main() {
    val lines = getInputLines("day24")
    var grid = mutableMapOf<Pair<Int, Int>, TileColor>()

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

    repeat(100) {
        val newGrid = grid.toMutableMap()
        grid.forEach { (coordinate, _) ->
            val (x, y) = coordinate
            checkCell(coordinate, grid, newGrid)
            adjacents.forEach { (xOff, yOff) ->
                checkCell(Pair(x + xOff, y + yOff), grid, newGrid)
            }
        }
        grid = newGrid
        println(it + 1)
        println(grid.values.count { it == TileColor.BLACK })
    }
}

fun checkCell(
    coordinate: Pair<Int, Int>,
    oldGrid: Map<Pair<Int, Int>, TileColor>,
    newGrid: MutableMap<Pair<Int, Int>, TileColor>
) {
    val (x, y) = coordinate
    val color = oldGrid[coordinate] ?: TileColor.WHITE
    val blackCount = adjacents.count { (xOff, yOff) ->
        val adjacent = oldGrid[Pair(x + xOff, y + yOff)] ?: TileColor.WHITE
        adjacent == TileColor.BLACK
    }
    newGrid[coordinate] = when (color) {
        TileColor.BLACK -> {
            when (blackCount) {
                0, in (3..Int.MAX_VALUE) -> TileColor.WHITE
                else -> TileColor.BLACK
            }
        }
        TileColor.WHITE -> {
            when (blackCount) {
                2 -> TileColor.BLACK
                else -> TileColor.WHITE
            }
        }
    }
}

val adjacents = listOf(
    Pair(-2, 0),
    Pair(-1, 1),
    Pair(1, 1),
    Pair(2, 0),
    Pair(1, -1),
    Pair(-1, -1)
)


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