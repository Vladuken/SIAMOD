package com.vladuken.siamod.distibutions

import kotlin.math.sqrt

class GaussDistribution(private val m: Double, private val sigma : Double, private val N : Int = 6) : Distribution {
    override fun transform(numbers: Collection<Double>): Collection<Double> {
        return numbers.map {
            var sum = 0.0
            repeat(N){
                sum+=numbers.random()
            }
            m + sigma * sqrt(12f/N)*(sum - N/2)
        }
    }
}