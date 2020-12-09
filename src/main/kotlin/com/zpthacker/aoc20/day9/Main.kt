package com.zpthacker.aoc20.day9

import com.zpthacker.aoc20.getInputLines

fun main() {
    val input = getInputLines("day9").map { it.toLong() }
    val vulnerableNumber = detectSum(input)
    println(vulnerableNumber)
    println(detectRange(input, vulnerableNumber))
}

val preamble = 25

fun detectSum(input: List<Long>): Long {
    var index = preamble
    while (index <= input.lastIndex) {
        val candidate = input[index]
        var found = false
        for (i in input.subList(index - preamble, index)) {
            if (found) { continue }
            for (j in input.subList(index - preamble, index)) {
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

fun detectRange(input: List<Long>, targetNumber: Long): Long {
    for (i in input.indices) {
        for (j in (i..input.count())) {
            val numbers = input.subList(i, j)
            if (numbers.sum() == targetNumber) {
                return numbers.minOrNull()!! + numbers.maxOrNull()!!
            }
        }
    }
    throw RuntimeException()
}
