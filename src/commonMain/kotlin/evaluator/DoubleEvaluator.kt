package evaluator

import Token

class DoubleEvaluator : BaseEvaluator() {

    fun evaluate(postfixExpression: List<Token>, values: Map<String, Any> = emptyMap()): Double {
        val evaluatedToken = evaluateInternal(postfixExpression, values)

        require(evaluatedToken is Token.Operand.Num)

        return evaluatedToken.value
    }
}