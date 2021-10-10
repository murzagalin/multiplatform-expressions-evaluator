# Multiplatform expressions evaluator

This is a kotlin multiplatform runtime [infix](https://en.wikipedia.org/wiki/Infix_notation) expressions evaluator.

## Overview

### Operators
The library supports the following operators and special symbols:

* `+`, `-`, `*`, `/`, `% (modulo)`, `^ (exponentiation)` - mathematical operators
* `&&`, `||`, `!` - logical 'and', 'or', 'not' operators
* `==`, `!=` - equality operators
* `<`, `>`, `<=`, `>=` - comparison operators
* `a ? b : c` - ternary operator

### Constants
The library supports the following constants:
* `pi` - [PI](https://en.wikipedia.org/wiki/Pi)
* `e` - [e](https://en.wikipedia.org/wiki/E_(mathematical_constant))
* any custom constants.

### Functions
The library supports the following functions:
* `abs(x)` - [absolute value](https://en.wikipedia.org/wiki/Absolute_value)
* `acos(x)` - [arccosine](https://en.wikipedia.org/wiki/Inverse_trigonometric_functions)
* `asin(x)` - [arcsine](https://en.wikipedia.org/wiki/Inverse_trigonometric_functions)
* `atan(x)` - [arctangent](https://en.wikipedia.org/wiki/Inverse_trigonometric_functions)
* `avg(a1, a2, a3, ..., an)` - average of N values
* `ceil(x)` - [ceiling](https://en.wikipedia.org/wiki/Floor_and_ceiling_functions)
* `cos(x)` - [cosine](https://en.wikipedia.org/wiki/Trigonometric_functions)
* `cosh(x)` - [hyperbolic cosine](https://en.wikipedia.org/wiki/Hyperbolic_functions)
* `floor(x)` - [floor](https://en.wikipedia.org/wiki/Floor_and_ceiling_functions)
* `ln(x)` - [natural logarithm](https://en.wikipedia.org/wiki/Natural_logarithm)
* `log(x, base)` - [logarithm](https://en.wikipedia.org/wiki/Logarithm)
* `max(a1, a2, a3, ..., an)` - maximum of N values
* `min(a1, a2, a3, ..., an)` - minimum of N values
* `round(x)` - [rounding to the nearest integer](https://en.wikipedia.org/wiki/Rounding#Rounding_to_the_nearest_integer)
* `sin(x)` - [sine](https://en.wikipedia.org/wiki/Trigonometric_functions)
* `sinh(x)` - [hyperbolic sine](https://en.wikipedia.org/wiki/Hyperbolic_functions)
* `sum(a1, a2, a3, ..., an)` - summation of N values
* `tan(x)` - [tangent](https://en.wikipedia.org/wiki/Trigonometric_functions)
* `tanh(x)` - [hyperbolic tangent](https://en.wikipedia.org/wiki/Trigonometric_functions)
* any custom function with any number of arguments

### Variables
Any symbols other than constants and function calls are resolved as variables during evaluation.

## How to get

### Gradle


#### Kotlin DSL
```kotlin
repositories {
    maven("https://maven.pkg.github.com/azamat-murzagalin/multiplatform-expressions-evaluator") {
        credentials {
            username = "<your github username>"
            password = "<your github token>"
        }
    }
}

dependencies {
    implementation("com.murzagalin:multiplatform-expressions-evaluator:0.8.3")
}
```

#### Groovy
```groovy
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/azamat-murzagalin/multiplatform-expressions-evaluator")
        credentials {
            username = "<your github username>"
            password = "<your github token>"
        }
    }
}

dependencies {
    implementation "com.murzagalin:multiplatform-expressions-evaluator:0.8.3"
}
```

## Kotlin example

```kotlin
import com.murzagalin.evaluator.Evaluator

val evaluator = Evaluator()

//numeric values
evaluator.evaluateDouble("23 + 0.123 * (124 / -60.124)")

//boolean values
evaluator.evaluateBoolean(
    "x > 1 && var",
    mapOf("x" to 3, "var" to true)
)

//variables
evaluator.evaluateDouble(
    "1 + x + y",
    mapOf("x" to 3, "y" to 4)
)

//functions
evaluator.evaluateDouble(
    "sin(x)^2+cos(x)^2",
    mapOf("x" to 2.0)
)

//constants
evaluator.evaluateDouble("cos(pi)")
evaluator.evaluateDouble("ln(e^3)")
```

### Custom functions

The library supports custom functions with any number of arguments.
Supported argument and return types are Double and Boolean

#### Functions with constant number of arguments 

For the example I will explain how to create a function that represents the general form of normal distribution probability density function:
![img.png](images/img.png)

The function will have the following syntax:
`normal_dist(x, m, sigma)`.
The parameter `m` is the mean of the distribution, while the parameter `sigma` is its standard deviation.

We define a function which is named "normal_dist" and has 3 arguments:

```kotlin
import com.murzagalin.evaluator.Function

object NormalDist : Function("normal_dist", 3) {
    override fun invoke(vararg args: Any): Any {
        val x = args[0]
        val m = args[1]
        val sigma = args[2]
        require(x is Double) { "$name argument must be a number" }
        require(m is Double) { "$name mean must be a number" }
        require(sigma is Double) { "$name sigma must be a number" }

        return 1.0 / sigma / sqrt(2 * PI) * exp(-1.0 / 2.0 * ((x - m) / sigma).pow(2))
    }
}
```

*Note:* the library checks if the number of arguments in an expression is equal to 3, otherwise it throws an exception. But you have to check the types of the arguments by yourself.

Then we add this function to the evaluator:

```kotlin
import com.murzagalin.evaluator.DefaultFunctions
import com.murzagalin.evaluator.Evaluator

fun main() {
    val evaluator = Evaluator(DefaultFunctions.ALL + NormalDist)
    print(evaluator.evaluateDouble("normal_dist(12, 9, 3)"))
}
```


#### Functions with variable number of arguments 

The process of creating functions with variable number of arguments is pretty much the same. The difference is how we define the function.

As an example I will create a function `mult(a1, a2, ..., an)` which is defined as `a1 * a2 * ... * an`:

```kotlin
object Mult: Function("mult", 2..Int.MAX_VALUE) {
    override fun invoke(vararg args: Any): Any {
        require(args.all { it is Double }) { "$name function requires all arguments to be numbers" }

        return args.fold(1.0) { acc, x -> acc * (x as Double) }
    }
}
```
*Note:* we define minimum and maximum number of arguments as a range. It is also possible to define them separately:
`Function("mult", 2, Int.MAX_VALUE)`

Then we add this function to the evaluator:
```kotlin
import com.murzagalin.evaluator.DefaultFunctions
import com.murzagalin.evaluator.Evaluator

fun main() {
    val evaluator = Evaluator(DefaultFunctions.ALL + Mult)
    print(evaluator.evaluateDouble("mult(2, 3, 4)"))
}
```