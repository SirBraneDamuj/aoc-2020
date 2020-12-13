package com.zpthacker.aoc20.day13

import com.zpthacker.aoc20.getInputLines

fun main() {
    val lines = getInputLines("day13")
    val (timeString, busIdString) = lines
    val buses = busIdString
        .split(",")
        .mapIndexedNotNull { index, busId ->
            if (busId == "x") {
                null
            } else {
                Pair(index.toLong(), busId.toLong())
            }
        }

    println(
        buses.joinToString(", ") { (i, id) ->
            "(x+$i) mod $id == 0"
        }
    )
}

