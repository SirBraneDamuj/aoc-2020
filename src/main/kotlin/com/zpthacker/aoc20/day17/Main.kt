package com.zpthacker.aoc20.day17

import com.zpthacker.aoc20.getInputLines

fun main() {
    val lines = getInputLines("day17")
    val threeDimensionalGrid = SpatialGrid(lines, ::Vector3)
    val fourDimensionalGrid = SpatialGrid(lines, ::Vector4)
    println("Part 1: Three dimenstional grid active after 6 simulations")
    threeDimensionalGrid.simulate(6)
    println(threeDimensionalGrid.currentActive)
    println("Part 2: Four dimenstional grid active after 6 simulations")
    fourDimensionalGrid.simulate(6)
    println(fourDimensionalGrid.currentActive)
}


class SpatialGrid<VectorClass : Neighborly<VectorClass>>(
    zeroSliceRows: List<String>,
    private val vectorBuilder: (Int, Int) -> VectorClass
) {
    private val initialSpace = mutableMapOf<VectorClass, Boolean>().apply {
        zeroSliceRows.forEachIndexed { i, row ->
            row.forEachIndexed { j, char ->
                this[vectorBuilder(j, i)] = when (char) {
                    '.' -> false
                    '#' -> true
                    else -> throw RuntimeException()
                }
            }
        }
    }
    private val steps = mutableListOf(initialSpace)

    val currentActive
        get() = steps.last().count { (_, state) -> state }

    fun simulate(times: Int) {
        repeat(times) { _ ->
            val previous = steps.last()
            val grid = previous.toMutableMap()
            val visited = mutableSetOf<VectorClass>()
            for ((coordinates, _) in previous) {
                checkNode(coordinates, visited, previous, grid)
                val neighbors = coordinates.neighbors
                neighbors.forEach { neighbor ->
                    checkNode(neighbor, visited, previous, grid)
                }
            }
            steps.add(grid)
        }
    }

    private fun checkNode(
        coordinates: VectorClass,
        visited: MutableSet<VectorClass>,
        previousGrid: Map<VectorClass, Boolean>,
        newGrid: MutableMap<VectorClass, Boolean>
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
}

interface Neighborly<A> {
    val neighbors: Set<A>
}

data class Vector3(
    val x: Int,
    val y: Int,
    val z: Int = 0
) : Neighborly<Vector3> {
    override val neighbors: Set<Vector3>
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

data class Vector4(
    val x: Int,
    val y: Int,
    val z: Int = 0,
    val w: Int = 0
) : Neighborly<Vector4> {
    override val neighbors: Set<Vector4>
        get() {
            val set = mutableSetOf<Vector4>()
            for (xOff in (-1..1)) {
                for (yOff in (-1..1)) {
                    for (zOff in (-1..1)) {
                        for (wOff in (-1..1)) {
                            set.add(Vector4(x + xOff, y + yOff, z + zOff, w + wOff))
                        }
                    }
                }
            }
            set.remove(this)
            return set
        }
}
