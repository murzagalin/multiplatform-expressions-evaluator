package com.murzagalin.evaluator

import kotlin.math.*

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

    val ABS = object: OneNumberArgumentFunction("abs", 1) {
        override fun invokeInternal(arg: Number) = abs(arg.toDouble())
    }

    val ACOS = object: OneNumberArgumentFunction("acos", 1) {
        override fun invokeInternal(arg: Number) = acos(arg.toDouble())
    }

    val ASIN = object: OneNumberArgumentFunction("asin", 1) {
        override fun invokeInternal(arg: Number) = asin(arg.toDouble())
    }

    val ATAN = object: OneNumberArgumentFunction("atan", 1) {
        override fun invokeInternal(arg: Number) = atan(arg.toDouble())
    }

    val COS = object: OneNumberArgumentFunction("cos", 1) {
        override fun invokeInternal(arg: Number) = cos(arg.toDouble())
    }

    val COSH = object: OneNumberArgumentFunction("cosh", 1) {
        override fun invokeInternal(arg: Number) = cosh(arg.toDouble())
    }

    val SINH = object: OneNumberArgumentFunction("sinh", 1) {
        override fun invokeInternal(arg: Number) = sinh(arg.toDouble())
    }

    val SIN = object: OneNumberArgumentFunction("sin", 1) {
        override fun invokeInternal(arg: Number) = sin(arg.toDouble())
    }

    val TAN = object: OneNumberArgumentFunction("tan", 1) {
        override fun invokeInternal(arg: Number) = tan(arg.toDouble())
    }

    val TANH = object: OneNumberArgumentFunction("tanh", 1) {
        override fun invokeInternal(arg: Number) = tanh(arg.toDouble())
    }

    val CEIL = object: OneNumberArgumentFunction("ceil", 1) {
        override fun invokeInternal(arg: Number) = ceil(arg.toDouble())
    }

    val FLOOR = object: OneNumberArgumentFunction("floor", 1) {
        override fun invokeInternal(arg: Number) = floor(arg.toDouble())
    }

    val ROUND = object: OneNumberArgumentFunction("round", 1) {
        override fun invokeInternal(arg: Number) = round(arg.toDouble())
    }

    val LN = object: OneNumberArgumentFunction("ln", 1) {
        override fun invokeInternal(arg: Number) = ln(arg.toDouble())
    }

    val LOG = object: Function("log", 2) {
        override fun invoke(args: List<Any>): Any {
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

    val MIN = object: Function("min", 2..Int.MAX_VALUE) {
        override fun invoke(args: List<Any>): Any {
            require(args.size > 1) {
                "$name should be called with at least 2 arguments"
            }
            require(args.all { it is Number }) {
                "$name function requires all arguments to be numbers"
            }

            return args.minByOrNull { (it as Number).toDouble() }!!
        }
    }

    val AVG = object: Function("avg", 2..Int.MAX_VALUE) {
        override fun invoke(args: List<Any>): Any {
            require(args.size > 1) {
                "$name should be called with at least 2 arguments"
            }
            require(args.all { it is Number }) {
                "$name function requires all arguments to be numbers"
            }

            return args.map { (it as Number).toDouble() }.average()
        }
    }

    val SUM = object: Function("sum", 2..Int.MAX_VALUE) {
        override fun invoke(args: List<Any>): Any {
            require(args.size > 1) {
                "$name should be called with at least 2 arguments"
            }
            require(args.all { it is Number }) {
                "$name function requires all arguments to be numbers"
            }

            return args.sumOf { (it as Number).toDouble() }
        }
    }

    val MAX = object: Function("max", 2..Int.MAX_VALUE) {
        override fun invoke(args: List<Any>): Any {
            require(args.size > 1) {
                "$name should be called with at least 2 arguments"
            }
            require(args.all { it is Number }) {
                "$name function requires all arguments to be numbers"
            }

            return args.maxByOrNull { (it as Number).toDouble() }!!
        }
    }

    val ALL = listOf(ABS, ACOS, ASIN, ATAN, COS, COSH, SINH, SIN, TAN, TANH, CEIL, FLOOR, ROUND, LN, LOG, MIN, MAX, AVG, SUM)
}