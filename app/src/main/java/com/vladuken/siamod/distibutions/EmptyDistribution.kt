package com.vladuken.siamod.distibutions

class EmptyDistribution : Distribution {
    override fun transform(numbers: Collection<Double>): Collection<Double> {
        return numbers
    }
}