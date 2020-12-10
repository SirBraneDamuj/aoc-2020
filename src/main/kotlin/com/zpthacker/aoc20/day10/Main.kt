package com.zpthacker.aoc20.day10

import com.zpthacker.aoc20.getInputLines

fun main() {
    val lines = getInputLines("day10")
    val joltages = lines.map(String::toInt).sorted()
    val diffs = countDiffs(joltages)
    println(diffs.getValue(1) * (diffs.getValue(3) + 1))
    val amendedList = listOf(0) + joltages + listOf(joltages.maxOrNull()!! + 3)
    val tree = buildTree(amendedList)
    println(countPaths(tree, joltages.maxOrNull()!!))
}

fun countDiffs(joltages: List<Int>): Map<Int, Int> {
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
    return diffs
}

fun buildTree(joltages: List<Int>): Map<Int, List<Int>> {
    return joltages.fold(mutableMapOf()) { acc, joltage ->
        acc[joltage] = joltages.filter { it < joltage && (joltage - it) <= 3 }
        acc
    }
}

val cache = mutableMapOf<Int, Long>()

fun countPaths(connections: Map<Int, List<Int>>, joltage: Int): Long {
    val cacheLookup = cache[joltage]
    return if (cacheLookup != null) {
        cacheLookup
    } else {
        val result = if (joltage == 0) {
            1L
        } else {
            var sum = 0L
            for (connection in connections.getValue(joltage)) {
                sum += countPaths(connections, connection)
            }
            sum
        }
        cache[joltage] = result
        result
    }
}
