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
        val sharedBorders = tile
            .borders
            .filter { border ->
                tiles.any { otherTile -> otherTile.id != tile.id && otherTile.adjacent(border) }
            }
        if (sharedBorders.count() == 2) {
            val (top, left, right, down) = tile.borders
            println(tile.id)
        }
        sharedBorders.count() == 2
    }
    val image = mutableListOf<MutableList<Tile>>().apply {
        repeat(3) {
            this.add(mutableListOf())
        }
        val tilesToCheck = tiles.toMutableList()
        val firstCorner = corners[0]
        firstCorner.flipY()
        this[0].add(firstCorner)
        tilesToCheck.removeIf { it.id == firstCorner.id }
        var rowNumber = 0
        while (true) {
            repeat(2) {
                val edge = this[rowNumber].last()
                val (_, _, right, _) = edge.borders
                val nextTile = tilesToCheck.find {
                    it.adjacent(right)
                }
                if (nextTile == null) {
                    println("oh dear")
                    error("")
                }
                val (_, direction, flip) = nextTile.adjacency(right)
                val numTurns = when (direction) {
                    Direction.UP -> 1
                    Direction.LEFT -> 0
                    Direction.RIGHT -> 2
                    Direction.DOWN -> 3
                }
                repeat(numTurns) { nextTile.turnCounterClockwise() }
                if (flip != Flip.NO_FLIP) { nextTile.flipY() }
                this[rowNumber].add(nextTile)
                tilesToCheck.removeIf { it.id == nextTile.id }
            }
            val leftBottomEdge = this[rowNumber].first()
            val (_, _, _, bottom) = leftBottomEdge.borders
            rowNumber++
            if (rowNumber > this.lastIndex) break
            val nextTile = tilesToCheck.single {
                it.adjacent(bottom)
            }
            val (_, direction, flip) = nextTile.adjacency(bottom)
            val numTurns = when (direction) {
                Direction.UP -> 0
                Direction.LEFT -> 3
                Direction.RIGHT -> 1
                Direction.DOWN -> 2
            }
            repeat(numTurns) { nextTile.turnCounterClockwise() }
            if (flip == Flip.NO_FLIP) { nextTile.flipX() }
            this[rowNumber].add(nextTile)
            tilesToCheck.removeIf { it.id == nextTile.id }
        }
    }
    println(image[0][0].id)
    println(image[0].last().id)
    println(image.last()[0].id)
    println(image.last().last().id)

    val noBorders = image.flatMap { tileRow ->
        val withoutBorders = tileRow.map(Tile::withoutBorders)
        val actualRows = mutableListOf<String>()
        for (i in 0 until 7) {
            actualRows.add(withoutBorders.joinToString("") { it[i] })
        }
        actualRows
    }
    val fRow = "                  # "
    val sRow = "#    ##    ##    ###"
    val tRow = " #  #  #  #  #  #   "
    var found = 0
    var rotated = noBorders.toMutableList().reversed()
    while (found == 0) {
        rotated.forEachIndexed { index, row ->
            if (index <= rotated.lastIndex - 2) {
                val f = indexOfMask(fRow, row)
                val s = indexOfMask(sRow, row)
                val t = indexOfMask(tRow, row)
                if (f != -1 && f == s && s == t) {
                    found++
                }
            }
        }
        rotated = mutableListOf<String>().apply {
            for (i in 9 downTo 0) {
                this.add(rotated.joinToString("") { it[i].toString() })
            }
        }
    }
    println(found)
}

fun indexOfMask(mask: String, row: String): Int {
    val window = row.windowed(mask.length).find { window ->
        window.indices.all { i->
            mask[i] != '#' || window[i] == '#'
        }
    }
    return if (window == null) {
        -1
    } else {
        row.indexOf(window)
    }
}

class Tile(
    val id: String,
    var grid: List<String>
) {
    val borders: List<String>
        get() = mutableListOf<String>().apply {
            // top
            this.add(grid.first())
            // left
            this.add(grid.joinToString("") { it.first().toString() }.reversed())
            // right
            this.add(grid.joinToString("") { it.last().toString() })
            // bottom
            this.add(grid.last().reversed())
        }

    fun adjacent(border: String) =
        borders.any {
            it == border ||
                    it == border.reversed() ||
                    it.reversed() == border ||
                    it.reversed() == border.reversed()
        }

    fun turnCounterClockwise() {
        grid = mutableListOf<String>().apply {
            for (i in 9 downTo 0) {
                this.add(grid.joinToString("") { it[i].toString() })
            }
        }
    }

    fun flipX() {
        grid = mutableListOf<String>().apply {
            repeat(10) { i ->
                this.add(grid[i].reversed())
            }
        }
    }

    fun flipY() {
        grid = grid.reversed()
    }

    fun adjacency(border: String): Triple<String, Direction, Flip> {
        val (top, left, right, bottom) = this.borders
        return when (border) {
            top -> {
                Triple(top, Direction.UP, Flip.NO_FLIP)
            }
            top.reversed() -> {
                Triple(top.reversed(), Direction.UP, Flip.FLIP_X)
            }
            left -> {
                Triple(left, Direction.LEFT, Flip.NO_FLIP)
            }
            left.reversed() -> {
                Triple(left.reversed(), Direction.LEFT, Flip.FLIP_Y)
            }
            right -> {
                Triple(right.reversed(), Direction.RIGHT, Flip.NO_FLIP)
            }
            right.reversed() -> {
                Triple(right.reversed(), Direction.RIGHT, Flip.FLIP_Y)
            }
            bottom -> {
                Triple(bottom, Direction.DOWN, Flip.NO_FLIP)
            }
            bottom.reversed() -> {
                Triple(bottom.reversed(), Direction.DOWN, Flip.FLIP_X)
            }
            else -> Triple("", Direction.UP, Flip.NO_FLIP)
        }
    }

    fun withoutBorders() = grid
        .drop(1)
        .dropLast(1)
        .map { row ->
            row.drop(1).dropLast(1)
        }
}

enum class Direction {
    UP,
    RIGHT,
    DOWN,
    LEFT,
}

enum class Flip {
    NO_FLIP,
    FLIP_X,
    FLIP_Y,
}
