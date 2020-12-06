package com.zpthacker.aoc20.day6

import com.zpthacker.aoc20.getInput

fun main() {
    val input = getInput("day6")
    val groups = input.split("\n\n")
    val groupAnswers = groups.map { it.split("\n") }
    val groupCounts = groupAnswers.map { answers ->
        answers
            .fold(mutableSetOf<Char>()) { acc, personAnswers ->
                acc.addAll(personAnswers.toList())
                acc
            }
            .count()
    }
    println(groupCounts.sum())
}
