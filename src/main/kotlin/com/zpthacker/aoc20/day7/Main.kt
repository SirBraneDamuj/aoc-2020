package com.zpthacker.aoc20.day7

import com.zpthacker.aoc20.getInputLines

fun main() {
    val lines = getInputLines("day7")
    val bagTypes = lines.fold(mutableMapOf<String, BagType>()) { acc, line ->
        val bagType = bagRuleForLine(line)
        acc[bagType.color] = bagType
        acc
    }
    val traverser = BagTypeTraverser(bagTypes)
    println(traverser.search("shiny gold", bagTypes["shiny aqua"]!!))
    println(traverser.count("shiny gold"))
}


fun bagRuleForLine(line: String): BagType {
    val (colorToken, rest) = line.split(Regex(" contain( |s )"))
    val color = colorToken.dropLast(5)
    return if (rest.contains("no other bags")) {
        BagType(color = color, contained = mapOf())
    } else {
        val contained = rest
            .split(", ")
            .fold(mutableMapOf<String, Int>()) { acc, rule ->
                val tokens = rule.split(" ").dropLast(1)
                val containedQuantity = tokens.first().toInt()
                val containedColor = tokens.drop(1).joinToString(" ")
                acc[containedColor] = containedQuantity
                acc
            }
        BagType(color = color, contained = contained)
    }
}

class BagTypeTraverser(
    private val bagTypes: Map<String, BagType>
) {
    fun count(targetColor: String): Int {
        return bagTypes.values.count { bagType ->
            search(targetColor, bagType)
        }
    }

    fun search(targetColor: String, bagType: BagType): Boolean {
        return if (bagType.contained.containsKey(targetColor)) {
            true
        } else {
            bagType.contained.keys.any {
                val bt = bagTypes[it]
                bt != null && search(targetColor, bt)
            }
        }
    }
}

class BagType(
    val color: String,
    val contained: Map<String, Int>
) {
}
