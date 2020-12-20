package com.zpthacker.aoc20.day20

import com.zpthacker.aoc20.getInput

fun main() {
    val tiles = getInput("day20")
        .split("\n\n")
        .map {
            val lines = it.split("\n").filter(String::isNotBlank)
            val id = lines.first().drop(5).dropLast(1)
            Tile(id, lines.drop(1))
        }
    val corners = tiles.filter { tile ->
        val sharedBordersCount = tile
            .borders
            .count { border ->
                val count = tiles.any { otherTile -> otherTile.id != tile.id && otherTile.adjacent(border) }
                count
            }
        sharedBordersCount == 2
    }
    println(corners.map { it.id.toLong() }.reduce(Long::times))
}

class Tile(
    val id: String,
    val grid: List<String>
) {
    val borders: List<String>
        get() = mutableListOf<String>().apply {
            // top
            this.add(grid.first())
            // left
            this.add(grid.joinToString("") { it.first().toString() })
            // right
            this.add(grid.joinToString("") { it.last().toString() })
            // bottom
            this.add(grid.last())
        }

    fun adjacent(border: String) =
        borders.any {
            it == border ||
                    it == border.reversed() ||
                    it.reversed() == border ||
                    it.reversed() == border.reversed()
        }
}

