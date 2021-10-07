package com.murzagalin.evaluator

internal class BooleanEvaluator : BaseEvaluator() {

    fun evaluate(postfixExpression: PreprocessedExpression, values: Map<String, Any> = emptyMap()): Boolean {
        val evaluatedToken = evaluateInternal(postfixExpression, values)

        require(evaluatedToken is Token.Operand.Bool)

        return evaluatedToken.value
    }
}