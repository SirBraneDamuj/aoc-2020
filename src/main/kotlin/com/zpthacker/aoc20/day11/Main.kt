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
    var numChanged = 0
    var count = 0
    do {
        count++
        simulate(rows)
        numChanged = countChanged(rows)
    } while (numChanged != 0)
    println(count)
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

fun adjacents(row: Int, col: Int): List<Pair<Int, Int>> {
    return listOf(
        Pair(row - 1, col - 1),
        Pair(row - 1, col),
        Pair(row - 1, col + 1),
        Pair(row, col - 1),
        Pair(row, col + 1),
        Pair(row + 1, col - 1),
        Pair(row + 1, col),
        Pair(row + 1, col + 1)
    )
}

fun countAdjacent(rows: List<List<GridCell>>, state: GridState, row: Int, col: Int): Int {
    val adjacentCells = adjacents(row, col)
    return adjacentCells.count { (adjacentRow, adjacentCol) ->
        adjacentRow in rows.indices
                && adjacentCol in rows.first().indices
                && rows[adjacentRow][adjacentCol].state == state
    }
}

fun handleEmpty(rows: List<List<GridCell>>, row: Int, col: Int) {
    val adjacentOccupied = countAdjacent(rows, GridState.OCCUPIED, row, col)
    if (adjacentOccupied == 0) {
        val cell = rows[row][col]
        cell.newState = GridState.OCCUPIED
        cell.changed = true
    }
}

fun handleOccupied(rows: List<List<GridCell>>, row: Int, col: Int) {
    val adjacentOccupied = countAdjacent(rows, GridState.OCCUPIED, row, col)
    if (adjacentOccupied >= 4) {
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
