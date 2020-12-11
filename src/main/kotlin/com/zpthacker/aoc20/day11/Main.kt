package com.zpthacker.aoc20.day11

import com.zpthacker.aoc20.getInputLines

fun main() {
    val lines = getInputLines("day11")
    val rows = lines.map { line ->
        line.map { cell ->
            GridCell(
                when (cell) {
                    '#' -> GridState.OCCUPIED
                    'L' -> GridState.EMPTY
                    else -> GridState.FLOOR
                }
            )
        }
    }
    var numChanged: Int
    var count = 0
    do {
        count++
        println(count)
        simulate(rows)
        numChanged = countChanged(rows)
    } while (numChanged != 0)
    val solution = rows.sumBy { row ->
        row.count { cell ->
            val occupied = cell.state == GridState.OCCUPIED
            occupied
        }
    }
    println(solution)
}

fun simulate(rows: List<List<GridCell>>) {
    for (i in rows.indices) {
        for (j in rows[i].indices) {
            val cell = rows[i][j]
            when (cell.state) {
                GridState.EMPTY -> handleEmpty(rows, i, j)
                GridState.OCCUPIED -> handleOccupied(rows, i, j)
                else -> continue
            }
        }
    }
}

enum class Direction(
    val rowInc: Int,
    val colInc: Int
) {
    UPLEFT(-1, -1),
    UP(-1, 0),
    UPRIGHT(-1, 1),
    LEFT(0, -1),
    RIGHT(0, 1),
    DOWNLEFT(1, -1),
    DOWN(1, 0),
    DOWNRIGHT(1, 1)
}

fun countVisible(rows: List<List<GridCell>>, state: GridState, row: Int, col: Int): Int {
    return Direction.values().count { direction ->
        var rowPos = row + direction.rowInc
        var colPos = col + direction.colInc
        var seen = false
        var found = false
        while (rowPos in rows.indices && colPos in rows.first().indices && !seen) {
            val cell = rows[rowPos][colPos]
            if (cell.state != GridState.FLOOR) {
                seen = true
                if (cell.state == state) {
                    found = true
                }
            } else {
                rowPos += direction.rowInc
                colPos += direction.colInc
            }
        }
        found
    }
}

fun handleEmpty(rows: List<List<GridCell>>, row: Int, col: Int) {
    val adjacentOccupied = countVisible(rows, GridState.OCCUPIED, row, col)
    if (adjacentOccupied == 0) {
        val cell = rows[row][col]
        cell.newState = GridState.OCCUPIED
        cell.changed = true
    }
}

fun handleOccupied(rows: List<List<GridCell>>, row: Int, col: Int) {
    val adjacentOccupied = countVisible(rows, GridState.OCCUPIED, row, col)
    if (adjacentOccupied >= 5) {
        val cell = rows[row][col]
        cell.newState = GridState.EMPTY
        cell.changed = true
    }
}

fun countChanged(rows: List<List<GridCell>>): Int {
    return rows.sumBy { row ->
        val count = row.count {
            val didChange = it.changed
            it.changed = false
            it.state = it.newState
            didChange
        }
        count
    }
}

class GridCell(
    var state: GridState,
    var changed: Boolean = false
) {
    var newState = state
}

enum class GridState {
    FLOOR,
    OCCUPIED,
    EMPTY
}
