package com.zpthacker.aoc20.day3

import com.zpthacker.aoc20.getInputLines
import java.math.BigInteger

fun main() {
    val lines = getInputLines("day3")
    val map = loadMap(lines)
    val traversers = listOf(
        MapTraverser(1, 1, map),
        MapTraverser(3, 1, map),
        MapTraverser(5, 1, map),
        MapTraverser(7, 1, map),
        MapTraverser(1, 2, map)
    )
    println("Part 1")
    println("3 right 1 down tree count: ${traversers[1].countTreesDuringTraversal()}")
    val result = traversers.fold(BigInteger.valueOf(1L)) { acc, traverser ->
        val treeCount = traverser.countTreesDuringTraversal()
        acc * BigInteger.valueOf(treeCount.toLong())
    }
    println("Part 2")
    println("All slopes' tree counts multiplied together: $result")
}

fun loadMap(lines: List<String>): TobogganMap {
    val trees = lines
        .map { line ->
            line.map { cell ->
                cell == '#'
            }
        }
    return TobogganMap(rows = trees)
}

enum class TraversalResult {
    SAFE,
    TREE,
    END
}

class TobogganMap(
    val rows: List<List<Boolean>>
) {
    private val width = rows.first().count()

    fun traverse(x: Int, y: Int): TraversalResult {
        val index = x % width
        return when {
            y >= rows.count() -> TraversalResult.END
            rows[y][index] -> TraversalResult.TREE
            else -> TraversalResult.SAFE
        }
    }
}

class MapTraverser(
    val right: Int,
    val down: Int,
    val map: TobogganMap
) {
    fun countTreesDuringTraversal(): Int {
        var result = TraversalResult.SAFE
        var x = 0
        var y = 0
        var trees = 0
        while (result != TraversalResult.END) {
            result = map.traverse(x, y)
            if (result == TraversalResult.TREE) {
                trees++
            }
            x += right
            y += down
        }
        return trees
    }
}
