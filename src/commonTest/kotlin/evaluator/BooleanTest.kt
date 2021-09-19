package evaluator

import evaluateBoolean
import kotlin.test.Test
import kotlin.test.assertEquals

class BooleanTest {

    @Test
    fun just_boolean_values() {
        assertEquals(true, "true".evaluateBoolean())
        assertEquals(false, "false".evaluateBoolean())
    }

    @Test
    fun less_than() {
        assertEquals(true, "1 < 2".evaluateBoolean())
        assertEquals(false, "23 < 23".evaluateBoolean())
        assertEquals(true, "23*0.5 < 23".evaluateBoolean())
        assertEquals(true, "2 <= 2".evaluateBoolean())
        assertEquals(false, "24 <= 23".evaluateBoolean())
    }

    @Test
    fun greater_than() {
        assertEquals(false, "1 > 2".evaluateBoolean())
        assertEquals(true, "23 > 23/2".evaluateBoolean())
        assertEquals(true, "2 >= 2".evaluateBoolean())
        assertEquals(false, "23 >= 24".evaluateBoolean())
    }

    @Test
    fun not() {
        assertEquals(false, "!true".evaluateBoolean())
        assertEquals(true, "!false".evaluateBoolean())
    }

    @Test
    fun and() {
        assertEquals(false, "true&&false".evaluateBoolean())
        assertEquals(true, "true&&!false".evaluateBoolean())
        assertEquals(true, "true&&true&&!false".evaluateBoolean())
    }

    @Test
    fun or() {
        assertEquals(true, "true||false".evaluateBoolean())
        assertEquals(false, "false||!true".evaluateBoolean())
        assertEquals(false, "false||false||!true".evaluateBoolean())
        assertEquals(true, "true||false||!true".evaluateBoolean())
    }

    @Test
    fun equal() {
        assertEquals(false, "1 == 2".evaluateBoolean())
        assertEquals(true, "1 != 2".evaluateBoolean())
        assertEquals(true, "12*2 != 25".evaluateBoolean())
        assertEquals(false, "12*2 != 24".evaluateBoolean())
        assertEquals(true, "12*2 == 48/2".evaluateBoolean())

        assertEquals(true, "true == true".evaluateBoolean())
        assertEquals(false, "true == false".evaluateBoolean())
        assertEquals(false, "true&&false != false".evaluateBoolean())
        assertEquals(true, "true||false != false&&true".evaluateBoolean())
    }

    @Test
    fun expressions() {
        assertEquals(false, "1 < 2 && var".evaluateBoolean(values = mapOf("var" to false)))
        assertEquals(true, "1 < 2 && !var".evaluateBoolean(values = mapOf("var" to false)))
        assertEquals(false, "!(1 < 2) && !var".evaluateBoolean(values = mapOf("var" to false)))
    }
}