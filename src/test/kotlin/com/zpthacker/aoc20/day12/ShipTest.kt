package com.zpthacker.aoc20.day12

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class ShipTest {
    @Test
    fun test_rotate360DoesNothing() {
        val ship = Ship(0, 0, 10, -1, Direction.EAST)
        ship.rotateWaypoint(360, TurnDirection.LEFT)
        assertEquals(ship.waypointX, 10)
        assertEquals(ship.waypointY, -1)
    }

    @Test
    fun test_rotate90Left() {
        val ship = Ship(0, 0, 10, -1, Direction.EAST)
        ship.rotateWaypoint(90, TurnDirection.LEFT)
        assertEquals(ship.waypointX, 1)
        assertEquals(ship.waypointY, 10)
    }

    @Test
    fun test_rotate180Flips() {
        val ship = Ship(0, 0, 10, -1, Direction.EAST)
        ship.rotateWaypoint(180, TurnDirection.LEFT)
        assertEquals(ship.waypointX, -10)
        assertEquals(ship.waypointY, 1)
    }

    @Test
    fun test_rotate270Left() {
        val ship = Ship(0, 0, 10, -1, Direction.EAST)
        ship.rotateWaypoint(270, TurnDirection.LEFT)
        assertEquals(ship.waypointX, -1)
        assertEquals(ship.waypointY, -10)
    }

    @Test
    fun test_rotate90Right() {
        val ship = Ship(0, 0, 10, -1, Direction.EAST)
        ship.rotateWaypoint(90, TurnDirection.RIGHT)
        assertEquals(ship.waypointX, -1)
        assertEquals(ship.waypointY, -10)
    }

    @Test
    fun test_rotate180RightFlips() {
        val ship = Ship(0, 0, 10, -1, Direction.EAST)
        ship.rotateWaypoint(180, TurnDirection.RIGHT)
        assertEquals(ship.waypointX, -10)
        assertEquals(ship.waypointY, 1)
    }

    @Test
    fun test_rotate270Right() {
        val ship = Ship(0, 0, 10, -1, Direction.EAST)
        ship.rotateWaypoint(270, TurnDirection.RIGHT)
        assertEquals(ship.waypointX, 1)
        assertEquals(ship.waypointY, 10)
    }
}
