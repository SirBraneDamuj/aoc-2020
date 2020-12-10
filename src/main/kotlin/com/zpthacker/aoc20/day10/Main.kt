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

fun countDiffs(joltages: List<Int>) =
    mutableMapOf(Pair(1, 0), Pair(2, 0), Pair(3, 0)).apply {
        for (i in joltages.indices) {
            val joltage = joltages[i]
            if (i == 0) {
                this.replace(joltage, 1)
            } else {
                val difference = joltages[i] - joltages[i - 1]
                this.replace(difference, this[difference]!!.inc())
            }
        }
    }

fun buildTree(joltages: List<Int>): Map<Int, List<Int>> =
    joltages
        .map { joltage ->
            Pair(joltage, joltages.filter { it < joltage && (joltage - it) <= 3 })
        }
        .toMap()

val cache = mutableMapOf<Int, Long>()

fun countPaths(connections: Map<Int, List<Int>>, joltage: Int): Long =
    cache.getOrPut(joltage) {
        if (joltage == 0) {
            1L
        } else {
            connections.getValue(joltage).fold(0L) { acc, connection ->
                acc + countPaths(connections, connection)
            }
        }
    }
