package com.zpthacker.aoc20.day14

import com.zpthacker.aoc20.getInputLines
import kotlin.math.pow

fun main() {
    val lines = getInputLines("day14")
    var mask = maskFromMaskLine(lines.first())
    val memory = mutableMapOf<Long, Long>()
    lines.drop(1).forEach { line ->
        when {
            line.startsWith("mask") -> {
                mask = maskFromMaskLine(line)
            }
            line.startsWith("mem") -> {
                val (addresses, value) = memoryAssignmentForLine(mask, line)
                addresses.forEach {
                    memory[it] = value
                }
            }
        }
    }
    val answer = memory.values.sum()
    println(answer)
}

fun maskFromMaskLine(line: String) = line.split(" ").last()

fun memoryAssignmentForLine(mask: String, line: String): Pair<List<Long>, Long> {
    val tokens = line.split(" ")
    val address = tokens
        .first()
        .drop(4)
        .dropLast(1)
        .toLong()
    val value = tokens.last().toLong()
    val bits = numberAsBits(address, 36)
    val addresses = mask
        .mapIndexed { index, bit ->
            when (bit) {
                '0' -> bits[index]
                '1' -> '1'
                else -> bit // 1
            }
        }
        .joinToString("")
        .split("X")
        .fold(listOf<String>()) { acc, segment ->
            val variations = listOf("0", "1").map {
                segment + it
            }
            if (acc.isEmpty()) {
                variations
            } else {
                acc.flatMap { prefix ->
                    variations.map { suffix ->
                        prefix + suffix
                    }
                }
            }
        }
        .map(::bitsAsNumber)
    return Pair(addresses, value)
}

fun numberAsBits(number: Long, numBits: Int): String {
    var x = number.toDouble()
    return ((numBits - 1) downTo 0).joinToString("") {
        val subtrahend = 2.0.pow(it)
        if (x >= subtrahend) {
            x -= subtrahend
            "1"
        } else {
            "0"
        }
    }
}

fun bitsAsNumber(bits: String): Long {
    return bits
        .foldIndexed(0.0) { index, acc, c ->
            if (c == '1') {
                acc + Math.pow(2.0, (35 - index).toDouble())
            } else {
                acc
            }
        }
        .toLong()
}
