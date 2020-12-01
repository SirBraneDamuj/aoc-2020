package com.zpthacker.aoc20.day1

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class MainKtTest {
    @Test
    fun test_twoValuesAddingToTarget() {
        val list = listOf(1721, 979, 366, 299, 675, 1456).sorted()
        val (x, y) = list.twoValuesAddingTo(2020)!!
        assertEquals(x, 299)
        assertEquals(y, 1721)
    }

    @Test
    fun test_nValuesAddingToTarget() {
        val list = listOf(1721, 979, 366, 299, 675, 1456).sorted()
        val (x, y, z) = nValuesAddingToTarget(list, 3, 2020)!!
        assertEquals(x, 366)
        assertEquals(y, 675)
        assertEquals(z, 979)
    }
}
