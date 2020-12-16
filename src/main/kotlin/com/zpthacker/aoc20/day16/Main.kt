package com.zpthacker.aoc20.day16

import com.zpthacker.aoc20.getInput
import java.lang.NumberFormatException
import java.text.NumberFormat

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
    val errorRate = otherTickets.fold(0) { acc, ticket ->
        var errors = 0
        ticket.forEach { field ->
            if (rules.values.all { ranges -> ranges.none { range -> range.contains(field) }}) {
                errors += field
            }
        }
        acc + errors
    }
    println(errorRate)
}
