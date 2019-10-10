package com.vladuken.siamod.distibutions

interface Distribution {
    fun transform(numbers: Collection<Double>) : Collection<Double>
}

