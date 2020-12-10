package com.zpthacker.aoc20.day10

import com.zpthacker.aoc20.getInputLines

fun main() {
    val lines = getInputLines("day10")
    val joltages = lines.map(String::toInt).sorted()
    val diffs = mutableMapOf(
        Pair(1, 0),
        Pair(2, 0),
        Pair(3, 0)
    )
    for (i in joltages.indices) {
        val joltage = joltages[i]
        if (i == 0) {
            diffs.replace(joltage, 1)
        } else {
            val difference = joltages[i] - joltages[i - 1]
            diffs.replace(difference, diffs[difference]!!.inc())
        }
    }
    println(diffs[1]!! * (diffs[3]!! + 1))
}
