package com.zpthacker.aoc20.day7

import com.zpthacker.aoc20.getInputLines

fun main() {
    val lines = getInputLines("day7")
    val bagMap = BagMap()
    lines.forEach { addNodeForLine(bagMap, it) }
    println("Part 1: How many bag colors contain at least one shiny bag?")
    println(bagMap.countPaths("shiny gold"))
    println("Part 2: How many bags does a single shiny gold bag contain?")
    println(bagMap.nodes["shiny gold"]!!.sum() - 1)
}


fun addNodeForLine(bagMap: BagMap, line: String) {
    val (colorToken, rest) = line.split(Regex(" contain( |s )"))
    val color = colorToken.dropLast(5)
    return if (rest.contains("no other bags")) {
        bagMap.addNode(color = color, edges = listOf())
    } else {
        val edges = rest
            .split(", ")
            .map { rule ->
                val tokens = rule.split(" ").dropLast(1)
                val containedQuantity = tokens.first().toInt()
                val containedColor = tokens.drop(1).joinToString(" ")
                BagEdge(color = containedColor, count = containedQuantity)
            }
        bagMap.addNode(color = color, edges = edges)
    }
}

class BagMap {
    val nodes = mutableMapOf<String, BagNode>()

    fun addNode(color: String, edges: List<BagEdge>) {
        nodes[color] = BagNode(edges)
    }

    fun countPaths(target: String): Int {
        return nodes.values.count { it.find(target) }
    }

    inner class BagNode(
        val edges: List<BagEdge>
    ) {
        fun find(target: String): Boolean {
            return edges.any { edge ->
                if (edge.color == target) {
                    true
                } else {
                    nodes[edge.color]?.find(target) ?: false
                }
            }
        }

        fun sum(): Int {
            return if (edges.count() == 0) {
                1
            } else {
                var count = 0
                edges.forEach { edge ->
                    val node = nodes[edge.color]!!
                    count += edge.count * node.sum()
                }
                1 + count
            }
        }
    }
}

class BagEdge(
    val color: String,
    val count: Int
)

