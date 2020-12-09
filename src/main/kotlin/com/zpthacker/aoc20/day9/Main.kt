package com.zpthacker.aoc20.day9

import com.zpthacker.aoc20.getInputLines

fun main() {
    val input = getInputLines("day9").map { it.toLong() }
    println(detectSum(input))
}

fun detectSum(input: List<Long>): Long {
    var index = 25
    while (index <= input.lastIndex) {
        val candidate = input[index]
        var found = false
        for (i in input) {
            if (found) { continue }
            for (j in input) {
                if (found) continue
                if (i + j == candidate) {
                    found = true
                }
            }
        }
        if (!found) {
            return candidate
        } else {
            index += 1
        }
    }
    throw RuntimeException()
}
