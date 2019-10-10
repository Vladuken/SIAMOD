package com.vladuken.siamod.distibutions

import kotlin.math.max

class Triangle(private val a: Double, private val b: Double) : Distribution {
    override fun transform(numbers: Collection<Double>): Collection<Double> {
        numbers.chunked(2)

        return numbers.map {
            a + (b - a) * max(it, numbers.random())
        }
    }
}