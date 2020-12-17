package com.zpthacker.aoc20.day17

import com.zpthacker.aoc20.getInputLines

fun main() {
    val lines = getInputLines("day17")
    val space = mutableMapOf<Vector3, Boolean>()
    for (i in lines.indices) {
        val line = lines[i]
        for (j in line.indices) {
            val char = line[j]
            space[Vector3(j, i, 0)] = when (char) {
                '.' -> false
                '#' -> true
                else -> throw RuntimeException()
            }
        }
    }
    val steps = mutableListOf(space)
    repeat(6) { _ ->
        val previous = steps.last()
        val grid = previous.toMutableMap()
        val visited = mutableSetOf<Vector3>()
        for ((coordinates, _) in previous) {
            checkNode(coordinates, visited, previous, grid)
            val neighbors = coordinates.neighbors
            neighbors.forEach { neighbor ->
                checkNode(neighbor, visited, previous, grid)
            }
        }
        steps.add(grid)
    }
    val result = steps.last().count { (_, value) -> value }
    println(result)
}

fun checkNode(
    coordinates: Vector3,
    visited: MutableSet<Vector3>,
    previousGrid: Map<Vector3, Boolean>,
    newGrid: MutableMap<Vector3, Boolean>
) {
    if (visited.contains(coordinates)) return
    else visited.add(coordinates)

    val state = previousGrid[coordinates] ?: false
    val activeNeighbors = coordinates.neighbors.filter { previousGrid[it] ?: false }.count()
    val newState = when (state) {
        true -> activeNeighbors == 2 || activeNeighbors == 3
        false -> activeNeighbors == 3
    }
    newGrid[coordinates] = newState
}

data class Vector3(
    val x: Int,
    val y: Int,
    val z: Int
) {
    val neighbors: Set<Vector3>
        get() {
            val set = mutableSetOf<Vector3>()
            for (xOff in (-1..1)) {
                for (yOff in (-1..1)) {
                    for (zOff in (-1..1)) {
                        set.add(Vector3(x + xOff, y + yOff, z + zOff))
                    }
                }
            }
            set.remove(this)
            return set
        }
}
