package com.zpthacker.aoc20.day5

import com.zpthacker.aoc20.getInputLines

/*
The first 7 characters will either be F or B; these specify exactly one of the 128 rows on the plane (numbered 0 through 127).
Each letter tells you which half of a region the given seat is in.
Start with the whole list of rows; the first letter indicates whether the seat is in the front (0 through 63) or the back (64 through 127).
The next letter indicates which half of that region the seat is in, and so on until you're left with exactly one row.
 */

fun main() {
    val lines = getInputLines("day5")
    val seats = MutableList(128 * 8) { false }
    lines.forEach {
        val rowNumber = getRowNumber(it.dropLast(3), (0..127).toList())
        val columnNumber = getRowNumber(it.takeLast(3), (0..7).toList())
        val seatId = rowNumber * 8 + columnNumber
        seats[seatId] = true
    }
    val missingSeatIds = (0 until seats.count())
        .filter { it != 0 && it != seats.lastIndex && !seats[it] && seats[it+1] && seats[it-1] }
    println(missingSeatIds.single())
}

tailrec fun getRowNumber(s: String, range: List<Int>): Int {
    return if (range.count() == 1) {
        range.single()
    } else {
        val newRange = if (s.first() == 'F' || s.first() == 'L') {
            range.take(range.size / 2)
        } else {
            range.takeLast(range.size / 2)
        }
        getRowNumber(s.drop(1), newRange)
    }
}
