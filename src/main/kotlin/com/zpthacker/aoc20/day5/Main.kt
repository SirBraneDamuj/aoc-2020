package com.zpthacker.aoc20.day5

import com.zpthacker.aoc20.getInputLines

fun main() {
    val lines = getInputLines("day5")
    val seats = lines.fold(MutableList(128 * 8) { false }) { acc, line ->
        val rowNumber = decodeBinarySpacePartition(line.dropLast(3), 0..127)
        val columnNumber = decodeBinarySpacePartition(line.takeLast(3), 0..7)
        val seatId = rowNumber * 8 + columnNumber
        acc.also { it[seatId] = true }
    }
    println("Part 1: Largest seat id")
    val maxSeatId = (0 until seats.count())
        .filter { seats[it] }
        .maxOrNull()
    println(maxSeatId)

    println("Part 2: My seat ID")
    println("The only one missing where both the one before and after are present")
    val missingSeatIds = (1 until seats.lastIndex)
        .filter { !seats[it] && seats[it + 1] && seats[it - 1] }
    println(missingSeatIds.single())
}

tailrec fun decodeBinarySpacePartition(s: String, range: IntRange): Int {
    return if (range.count() == 1) {
        range.single()
    } else {
        val midpoint = (range.first + range.last) / 2
        val newRange = if (s.first() == 'F' || s.first() == 'L') {
            range.first..midpoint
        } else {
            (midpoint + 1)..range.last
        }
        decodeBinarySpacePartition(s.drop(1), newRange)
    }
}
