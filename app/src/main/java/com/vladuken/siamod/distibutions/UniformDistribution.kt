package com.vladuken.siamod.distibutions

class UniformDistribution(private val a: Double, private val b: Double) : Distribution {
    override fun transform(
        numbers: Collection<Double>
    ): Collection<Double> {
        return numbers.map {
            a + (b - a) * it
        }
    }
}
