package evaluator

import Token

class BooleanEvaluator : BaseEvaluator() {

    fun evaluate(postfixExpression: List<Token>, values: Map<String, Any> = emptyMap()): Boolean {
        val evaluatedToken = evaluateInternal(postfixExpression, values)

        require(evaluatedToken is Token.Operand.Bool)

        return evaluatedToken.value
    }
}