package com.vladuken.siamod.distibutions

class Simpson(private val a: Double, private val b: Double) : Distribution {
    override fun transform(numbers: Collection<Double>): Collection<Double> {
        val newList = UniformDistribution(a/2,b/2).transform(numbers)

        return numbers.map {
            newList.random() + newList.random()
        }
    }
}