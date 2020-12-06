package com.zpthacker.aoc20.day6

import com.zpthacker.aoc20.getInput

fun main() {
    val input = getInput("day6")
    val groups = input
        .split("\n\n")
        .map { group ->
            group
                .split("\n")
                .filter { personAnswers -> personAnswers.isNotBlank() }
                .map { personAnswers -> personAnswers.toSet() }
        }
    println("Part 1")
    val unionSum = groups
        .sumBy { group ->
            group.reduce(Set<Char>::union).count()
        }
    println("Union sum: $unionSum")
    println("Part 2")
    val intersectSum = groups
        .sumBy { group ->
            group.reduce(Set<Char>::intersect).count()
        }
    println("Intersect sum: $intersectSum")
}
