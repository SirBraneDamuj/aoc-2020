package com.zpthacker.aoc20.day8

import com.zpthacker.aoc20.getInputLines
import java.lang.IllegalArgumentException

fun main() {
    val lines = getInputLines("day8")
    val instructions = lines.map(::getInstructionForLine)
    val loopState = detectLoop(instructions)
    println(loopState.accumulator)
}

fun detectLoop(instructions: List<Instruction>): State {
    val state = State(0, 0)
    val visitedInstructions = mutableSetOf<Int>()
    while (state.pointer <= instructions.lastIndex) {
        val instruction = instructions[state.pointer]
        if (visitedInstructions.contains(state.pointer)) {
            return state
        } else {
            visitedInstructions.add(state.pointer)
            instruction.execute(state)
        }
    }
    throw RuntimeException()
}

fun getInstructionForLine(line: String): Instruction {
    val (type, arg) = line.split(" ")
    val instructionType = when (type) {
        "jmp" -> InstructionType.JUMP
        "acc" -> InstructionType.ACC
        "nop" -> InstructionType.NOP
        else -> throw IllegalArgumentException()
    }
    return Instruction(
        type = instructionType,
        argument = arg.toInt()
    )
}

enum class InstructionType {
    JUMP,
    ACC,
    NOP,
}

class State(
    var accumulator: Int,
    var pointer: Int
)

class Instruction(
    val type: InstructionType,
    val argument: Int
) {
    fun execute(state: State) {
        when (type) {
            InstructionType.JUMP -> {
                state.pointer += argument
            }
            InstructionType.ACC -> {
                state.accumulator += argument
                state.pointer += 1
            }
            else -> {
                state.pointer += 1
            }
        }
    }
}
