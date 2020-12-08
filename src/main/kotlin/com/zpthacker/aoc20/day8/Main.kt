package com.zpthacker.aoc20.day8

import com.zpthacker.aoc20.getInputLines

fun main() {
    val lines = getInputLines("day8")
    val instructions = lines.map(::getInstructionForLine)
    println("Part 1: Accumulator value just before the loop begins")
    val loopState = detectLoop(instructions)
    println(loopState.accumulator)

    println("Part 2: Accumulator value when the loop has been fixed")
    val correctedState = correctLoop(instructions)
    println(correctedState.accumulator)
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

fun detectLoop(instructions: List<Instruction>): State {
    val state = State()
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
    state.halted = true
    return state
}

fun correctLoop(instructions: List<Instruction>): State {
    for (index in instructions.indices) {
        val instruction = instructions[index]
        if (instruction.type == InstructionType.ACC) {
            continue
        }
        val candidateProgram = instructions.toMutableList()
        candidateProgram[index] = Instruction(
            type = if (instruction.type == InstructionType.NOP) InstructionType.JUMP else InstructionType.NOP,
            argument = instruction.argument
        )
        val potentialLoopState = detectLoop(candidateProgram)
        if (potentialLoopState.halted) {
            return potentialLoopState
        }
    }
    throw RuntimeException("The loop could not be corrected.")
}

enum class InstructionType {
    JUMP,
    ACC,
    NOP,
}

class State {
    var accumulator: Int = 0
    var pointer: Int = 0
    var halted: Boolean = false
}

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
