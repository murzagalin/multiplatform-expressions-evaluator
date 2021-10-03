package com.murzagalin.evaluator

import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.log
import kotlin.math.sin

abstract class Function(
    val name: String,
    val argsCount: IntRange
) {

    constructor(name: String, argsCount: Int): this(name, argsCount..argsCount)

    constructor(name: String, minArgsCount: Int, maxArgsCount: Int): this(name, minArgsCount..maxArgsCount)

    abstract operator fun invoke(args: List<Any>): Any
}

abstract class OneNumberArgumentFunction(name: String, argsCount: IntRange) : Function(name, argsCount) {

    constructor(name: String, argsCount: Int): this(name, argsCount..argsCount)

    constructor(name: String, minArgsCount: Int, maxArgsCount: Int): this(name, minArgsCount..maxArgsCount)

    override fun invoke(args: List<Any>): Any {
        require(args.size == 1) {
            "$name function requires 1 argument"
        }
        val operand = args[0]
        require(operand is Number) {
            "$name is called with argument type ${kotlin.Number::class.simpleName}, but supports only numbers"
        }

        return invokeInternal(operand)
    }

    abstract fun invokeInternal(arg: Number): Double
}

object DefaultFunctions {

    val COS = object: OneNumberArgumentFunction("cos", 1) {
        override fun invokeInternal(arg: Number) = cos(arg.toDouble())
    }

    val SIN = object: OneNumberArgumentFunction("sin", 1) {
        override fun invokeInternal(arg: Number) = sin(arg.toDouble())
    }

    val LN = object: OneNumberArgumentFunction("ln", 1) {
        override fun invokeInternal(arg: Number) = ln(arg.toDouble())
    }

    val LOG = object: Function("log", 2) {
        override fun invoke(args: List<Any>): Any {
            require(args.size == 2) {
                "$name function requires 2 argument"
            }

            val operand = args[0]
            val base = args[1]
            require(operand is Number) {
                "$name argument must be a number"
            }
            require(base is Number) {
                "$name base must be a number"
            }

            return log(operand.toDouble(), base.toDouble())
        }
    }

    val ALL = listOf(COS, SIN, LN, LOG)
}