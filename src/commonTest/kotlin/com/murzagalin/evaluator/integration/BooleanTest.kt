package com.murzagalin.evaluator.integration

import com.murzagalin.evaluator.Evaluator
import kotlin.test.Test
import kotlin.test.assertEquals

class BooleanTest {

    private val evaluator = Evaluator()

    @Test
    fun just_boolean_values() {
        assertEquals(true, evaluator.evaluateBoolean("true"))
        assertEquals(false, evaluator.evaluateBoolean("false"))
    }

    @Test
    fun less_than() {
        assertEquals(true, evaluator.evaluateBoolean("1 < 2"))
        assertEquals(false, evaluator.evaluateBoolean("23 < 23"))
        assertEquals(true, evaluator.evaluateBoolean("23*0.5 < 23"))
        assertEquals(true, evaluator.evaluateBoolean("2 <= 2"))
        assertEquals(false, evaluator.evaluateBoolean("24 <= 23"))
    }

    @Test
    fun greater_than() {
        assertEquals(false, evaluator.evaluateBoolean("1 > 2"))
        assertEquals(true, evaluator.evaluateBoolean("23 > 23/2"))
        assertEquals(true, evaluator.evaluateBoolean("2 >= 2"))
        assertEquals(false, evaluator.evaluateBoolean("23 >= 24"))
    }

    @Test
    fun not() {
        assertEquals(false, evaluator.evaluateBoolean("!true"))
        assertEquals(true, evaluator.evaluateBoolean("!false"))
    }

    @Test
    fun and() {
        assertEquals(false, evaluator.evaluateBoolean("true&&false"))
        assertEquals(true, evaluator.evaluateBoolean("true&&!false"))
        assertEquals(true, evaluator.evaluateBoolean("true&&true&&!false"))
    }

    @Test
    fun or() {
        assertEquals(true, evaluator.evaluateBoolean("true||false"))
        assertEquals(false, evaluator.evaluateBoolean("false||!true"))
        assertEquals(false, evaluator.evaluateBoolean("false||false||!true"))
        assertEquals(true, evaluator.evaluateBoolean("true||false||!true"))
    }

    @Test
    fun equal() {
        assertEquals(false, evaluator.evaluateBoolean("1 == 2"))
        assertEquals(true, evaluator.evaluateBoolean("1 != 2"))
        assertEquals(true, evaluator.evaluateBoolean("12*2 != 25"))
        assertEquals(false, evaluator.evaluateBoolean("12*2 != 24"))
        assertEquals(true, evaluator.evaluateBoolean("12*2 == 48/2"))

        assertEquals(true, evaluator.evaluateBoolean("true == true"))
        assertEquals(false, evaluator.evaluateBoolean("true == false"))
        assertEquals(false, evaluator.evaluateBoolean("true&&false != false"))
        assertEquals(true, evaluator.evaluateBoolean("true||false != false&&true"))
    }

    @Test
    fun expressions() {
        assertEquals(false, evaluator.evaluateBoolean("1 < 2 && var", values = mapOf("var" to false)))
        assertEquals(true, evaluator.evaluateBoolean("1 < 2 && !var", values = mapOf("var" to false)))
        assertEquals(false, evaluator.evaluateBoolean("!(1 < 2) && !var", values = mapOf("var" to false)))
        assertEquals(true, evaluator.evaluateBoolean("x > 1 && var", values = mapOf("x" to 3, "var" to true)))
    }
}