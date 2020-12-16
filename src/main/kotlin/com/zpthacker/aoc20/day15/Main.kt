package com.zpthacker.aoc20.day15

fun main() {
    val input = "8,0,17,4,1,12"
    val numbers = input.split(",")
    val speakings = numbers.map(String::toInt).toMutableList()
    val numberLookup = mutableMapOf<Int, MutableList<Int>>()
    speakings.forEachIndexed { index, x ->
        numberLookup[x] = mutableListOf(index)
    }
    for (i in (6..29999999)) {
        val number = speakings[i - 1]
        val lookup = numberLookup[number]
        val (last, previous) = when {
            lookup != null && lookup.count() == 1 -> {
                Pair(0, 0)
            }
            lookup != null && lookup.count() > 1 -> {
                val last = lookup.last()
                val previous = lookup[lookup.lastIndex - 1]
                Pair(last, previous)
            }
            else -> throw RuntimeException()
        }
        val value = last - previous
        if (numberLookup[value] == null) {
            numberLookup[value] = mutableListOf(i)
        } else {
            numberLookup[value]!!.add(i)
        }
        speakings.add(value)
    }
    println(speakings.last())
}
