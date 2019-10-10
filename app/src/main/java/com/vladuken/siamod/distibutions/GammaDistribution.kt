package com.vladuken.siamod.distibutions

import kotlin.math.ln

class GammaDistribution(lambda: Double, private val n: Int) : Exponential(lambda) {
    override fun transform(numbers: Collection<Double>): Collection<Double> {
        return numbers.map {
            var multiplication = 1.0
            repeat(n) {
                multiplication *= numbers.random()
            }

            -1 / lambda * ln(multiplication)
        }
    }
}