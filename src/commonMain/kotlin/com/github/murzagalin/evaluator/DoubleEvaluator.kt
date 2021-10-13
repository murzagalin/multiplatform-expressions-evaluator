package com.github.murzagalin.evaluator

internal class DoubleEvaluator : BaseEvaluator() {

    fun evaluate(postfixExpression: PreprocessedExpression, values: Map<String, Any> = emptyMap()): Double {
        val evaluatedToken = evaluateInternal(postfixExpression, values)

        require(evaluatedToken is Token.Operand.Number)

        return evaluatedToken.value
    }
}