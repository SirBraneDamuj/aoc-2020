package com.zpthacker.aoc20.day12

import com.zpthacker.aoc20.getInputLines
import kotlin.math.absoluteValue

fun main() {
    val lines = getInputLines("day12")
    val ship = Ship(0, 0, 10, 1, Direction.EAST)
    lines.forEach {
        val instruction = it[0]
        val argument = it.drop(1)
        if ("NSEW".contains(instruction)) {
            val direction = when (instruction) {
                'N' -> Direction.NORTH
                'E' -> Direction.EAST
                'S' -> Direction.SOUTH
                'W' -> Direction.WEST
                else -> throw RuntimeException()
            }
            // ship.move(argument.toInt(), direction)
            ship.moveWaypoint(argument.toInt(), direction)
        } else if (instruction == 'F') {
            ship.moveTowardWaypoint(argument.toInt())
        } else {
            val turnDirection = when (instruction) {
                'L' -> TurnDirection.LEFT
                'R' -> TurnDirection.RIGHT
                else -> throw RuntimeException()
            }
            // ship.turn(argument.toInt(), turnDirection)
            ship.rotateWaypoint(argument.toInt(), turnDirection)
        }
    }
    println(ship.x.absoluteValue + ship.y.absoluteValue)
}

enum class Direction(
    val xFactor: Int,
    val yFactor: Int,
    val sortOrder: Int
) {
    NORTH(0, 1, 0),
    EAST(1, 0, 1),
    SOUTH(0, -1, 2),
    WEST(-1, 0, 3)
}

enum class TurnDirection(
    val degreeFactor: Int
) {
    LEFT(-1),
    RIGHT(1)
}

class Ship(
    var x: Int,
    var y: Int,
    var waypointX: Int,
    var waypointY: Int,
    var facing: Direction
) {
    fun move(distance: Int, direction: Direction?) {
        val theDirection = direction ?: facing
        x += distance * theDirection.xFactor
        y += distance * theDirection.yFactor
    }

    fun moveTowardWaypoint(times: Int) {
        x += (times * waypointX)
        y += (times * waypointY)
    }

    fun moveWaypoint(distance: Int, direction: Direction) {
        waypointX += (distance * direction.xFactor)
        waypointY += (distance * direction.yFactor)
    }

    fun turn(degrees: Int, turnDirection: TurnDirection) {
        var turnRemaining = degrees / 90
        val directions = Direction.values().sortedBy { it.sortOrder }
        var currentIndex = directions.indexOfFirst { it == this.facing }
        while (turnRemaining != 0) {
            currentIndex += turnDirection.degreeFactor
            if (currentIndex == -1) {
                currentIndex = directions.lastIndex
            } else if (currentIndex == directions.count()) {
                currentIndex = 0
            }
            turnRemaining--
        }
        this.facing = directions[currentIndex]
    }

    fun rotateWaypoint(degrees: Int, turnDirection: TurnDirection) {
        var turnRemaining = degrees / 90
        while (turnRemaining != 0) {
            val x0 = waypointX
            val y0 = waypointY
            if (turnDirection == TurnDirection.LEFT) {
                waypointX = -y0
                waypointY = x0
            } else {
                waypointX = y0
                waypointY = -x0
            }
            turnRemaining--
        }
    }
}
