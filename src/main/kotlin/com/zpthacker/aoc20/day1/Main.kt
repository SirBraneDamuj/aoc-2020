package com.zpthacker.aoc20.day1

import com.zpthacker.aoc20.getInputLines

fun main() {
    val ints = getInputLines("day1")
        .map(String::toInt)
        .sorted()
    println("Part 1: Two values adding to 2020")
    val (x, y) = ints.twoValuesAddingTo(2020) ?: throw RuntimeException("dang")
    println("Values: $x $y.")
    println("Multiplied: ${x * y}")

    println("Part 2: Three values adding to 2020")
    val (a, b, c) = nValuesAddingToTarget(ints, 3, 2020) ?: throw RuntimeException("part 2 dang")
    println("Three values: $a $b $c")
    println("Multiplied: ${a * b * c}")

    println("And just to be sure...")
    println("Part 1 done using part 2's solution.")
    val (p, q) = nValuesAddingToTarget(ints, 2, 2020) ?: throw RuntimeException("my hubris dang")
    println("p and q: $p $q")
    val message = if (p == x && q == y) "equal" else "not equal"
    println("Those are $message to x and y!")
}

// part 1
fun List<Int>.twoValuesAddingTo(target: Int): Pair<Int, Int>? {
    var pos = 0
    while (pos <= this.lastIndex) {
        val x = this[pos]
        var comparePos = pos
        while (comparePos <= this.lastIndex) {
            val y = this[comparePos]
            if (x + y == target) {
                return Pair(x, y)
            }
            comparePos++
        }
        pos++
    }
    return null
}

//part 2
fun nValuesAddingToTarget(ints: List<Int>, n: Int, target: Int): List<Int>? {
    if (n == 1) {
        return if (ints.contains(target)) {
            listOf(target)
        } else {
            null
        }
    } else {
        for (i in ints) {
            val result = nValuesAddingToTarget(
                ints = ints,
                n = n - 1,
                target = target - i
            )
            if (result != null) {
                return listOf(i) + result
            }
        }
        return null
    }
}
