# Kotlin multiplatform expressions evaluator

This is a kotlin multiplatform runtime [infix](https://en.wikipedia.org/wiki/Infix_notation) expressions evaluator.

## Overview

### Operators
The library supports the following operators and special symbols:

* `+`, `-`, `*`, `/` - mathematical operators
* `%` - [modulo](https://en.wikipedia.org/wiki/Modulo_operation). Returns the remainder of a division, after one number is divided by another
*  `^` - exponentiation. `a^b` means `a raised to the power of b` 
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
Kotlin DSL
```kotlin
repositories {
    maven("https://maven.pkg.github.com/murzagalin/multiplatform-expressions-evaluator")
}

dependencies {
    implementation("com.github.murzagalin:multiplatform-expressions-evaluator:0.9.1")
}
```
Groovy
```groovy
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/murzagalin/multiplatform-expressions-evaluator")
    }
}

dependencies {
    implementation "com.github.murzagalin:multiplatform-expressions-evaluator:0.9.1"
}
```

## Kotlin

### Usage

```kotlin
import com.github.murzagalin.evaluator.Evaluator

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
Supported argument and return types are `Double` and `Boolean`

#### Functions with constant number of arguments 

For the example I will explain how to create a function that represents the general form of normal distribution probability density function:
![img.png](images/img.png)

The function will have the following syntax:
`normal_dist(x, m, sigma)`.
The parameter `m` is the mean of the distribution, while the parameter `sigma` is its standard deviation.

We define a function which is named "normal_dist" and has 3 arguments:

```kotlin
import com.github.murzagalin.evaluator.Function

object NormalDist : Function("normal_dist", 3) {
    override fun invoke(vararg args: Any): Any {
        val x = args.getAsDouble(0) { "$name argument must be a number" }
        val m = args.getAsDouble(1) { "$name mean must be a number" }
        val sigma = args.getAsDouble(2) { "$name sigma must be a number" }

        return 1.0 / sigma / sqrt(2 * PI) * exp(-1.0 / 2.0 * ((x - m) / sigma).pow(2))
    }
}
```

*Note:* the library checks if the number of arguments in an expression is equal to 3, otherwise it throws an exception. But you have to check the types of the arguments by yourself.
Functions `getAsDouble(index, lazyMessage)` and `getAsBoolean(index, lazyMessage)` return an element at position `index`, and throw `IllegalArgumentException` with the message returned from `lazyMessage` if it has a wrong type

Then we add this function to the evaluator:

```kotlin
import com.github.murzagalin.evaluator.DefaultFunctions
import com.github.murzagalin.evaluator.Evaluator

fun main() {
    val evaluator = Evaluator(functions = DefaultFunctions.ALL + NormalDist)
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
import com.github.murzagalin.evaluator.DefaultFunctions
import com.github.murzagalin.evaluator.Evaluator

fun main() {
    val evaluator = Evaluator(functions = DefaultFunctions.ALL + Mult)
    print(evaluator.evaluateDouble("mult(2, 3, 4)"))
}
```

### Custom constants

The library supports custom constants. For the example I will show you how to add a golden ratio constant.
We will define the constant named `phi` with the value `1.6180339887`:
```kotlin
import com.github.murzagalin.evaluator.Constant
import com.github.murzagalin.evaluator.DefaultConstants
import com.github.murzagalin.evaluator.Evaluator

fun main() {
    val PHI = Constant("phi", 1.6180339887)
    val evaluator = Evaluator(constants = DefaultConstants.ALL + PHI)
    print(evaluator.evaluateDouble("x * phi", mapOf("x" to 2)))
}
```

### Expressions preprocessing

By default, the library does the following steps to evaluate an expression:
1. Tokenizing - splitting the expression into a list of units (operations, numbers, constants, function calls, etc.)
2. Converting the expression from [infix notation](https://en.wikipedia.org/wiki/Infix_notation) to [abstract syntax tree](https://en.wikipedia.org/wiki/Abstract_syntax_tree).
3. Evaluating the abstract syntax tree.

In case you have an expression with variables, it might make sense to preprocess the expression (do steps 1 and 2 in advance) to improve the performance:
```kotlin
import com.github.murzagalin.evaluator.Evaluator

fun main() {
    val evaluator = Evaluator()
    
    //step 1 and 2
    val preprocessedExpression = evaluator.preprocessExpression("1 + x + y^2")
    
    //step 3
    val result = evaluator.evaluateDouble(preprocessedExpression, mapOf("x" to 2, "y" to 4))
}
```

## JVM
`TBD`

## JS
`TBD`

## IOS
`TBD`

## License
This library is available for free under [Apache 2.0 license](https://www.apache.org/licenses/LICENSE-2.0).

```
Copyright (c) 2021 Azamat Murzagalin.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```