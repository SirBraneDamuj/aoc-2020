package com.zpthacker.aoc20.day6

import com.zpthacker.aoc20.getInput

fun main() {
    val input = getInput("day6")
    val groups = input.split("\n\n")
    val groupAnswers = groups.map { it.split("\n").filter { person -> person.isNotBlank() } }
    val groupCounts = groupAnswers.map { people ->
        people
            .map { personAnswers -> personAnswers.toSet() }
            .reduce { acc, set -> acc.intersect(set) }
            .count()
    }
    println(groupCounts.sum())
}
