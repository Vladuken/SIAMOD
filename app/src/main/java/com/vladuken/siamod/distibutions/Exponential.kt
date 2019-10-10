package com.vladuken.siamod.distibutions

import kotlin.math.ln

open class Exponential(protected val lambda: Double) : Distribution {
    override fun transform(numbers: Collection<Double>): Collection<Double> {
        return numbers.map {
            -1/lambda* ln(1-it)
        }
    }
}