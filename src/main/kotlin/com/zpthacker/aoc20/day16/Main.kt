package com.zpthacker.aoc20.day16

import com.zpthacker.aoc20.getInput
import java.lang.RuntimeException

fun main() {
    val lines = getInput("day16")
    val chunks = lines.split("\n\n")
    val ticketRules = chunks.first().split("\n")
    val rules = ticketRules.fold(mutableMapOf<String, List<IntRange>>()) { acc, ruleLine ->
        val (fieldName, rest) = ruleLine.split(":")
        val (firstRange, secondRange) = rest
            .split(" ")
            .mapNotNull {
                if (it == "or" || it == "") null
                else {
                    val (first, last) = it.split("-").map(String::toInt)
                    first..last
                }
            }
        acc[fieldName] = listOf(firstRange, secondRange)
        acc
    }
    val myTicket = chunks[1].split("\n").drop(1).single().split(",").map(String::toInt)
    val otherTickets = chunks[2].split("\n").drop(1).dropLast(1).map {
        try {
            it.split(",").map(String::toInt)
        } catch (e: Exception) {
            println(e)
            throw e
        }
    }
    val validTickets = otherTickets.filter { ticket ->
        ticket.none { field ->
            rules.values.all { ranges -> ranges.none { range -> range.contains(field) } }
        }
    }
    val countingRules = rules.map { (fieldName, ranges) ->
        TicketRule(fieldName, ranges, myTicket.count())
    }
    for (ticket in validTickets) {
        for (rule in countingRules) {
            for (position in ticket.indices) {
                val field = ticket[position]
                if (rule.ranges.none { range -> range.contains(field) }) {
                    rule.removeInvalidPosition(position)
                }
            }
        }
    }
    val positions = mutableMapOf<String, Int>()
    while (positions.count() != 20) {
        val next = countingRules.find { it.validPositions.count() == 1 } ?: throw RuntimeException()
        val position = next.validPositions.single()
        positions[next.fieldName] = next.validPositions.single()
        countingRules.forEach { it.removeInvalidPosition(position)}
    }
    val result = positions
        .filter { (fieldName, _) -> fieldName.startsWith("departure") }
        .map { (_, position) -> myTicket[position].toLong() }
    println(result.reduce(Long::times))
}

class TicketRule(
    val fieldName: String,
    val ranges: List<IntRange>,
    numPositions: Int
) {
    val validPositions = (0 until numPositions).toMutableSet()

    fun removeInvalidPosition(position: Int) {
        validPositions.remove(position)
    }
}
