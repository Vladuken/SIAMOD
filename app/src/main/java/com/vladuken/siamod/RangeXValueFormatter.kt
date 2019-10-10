package com.vladuken.siamod

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class RangeXValueFormatter(numbers: Iterable<Double>, val numGroups: Int) : ValueFormatter() {
    private val step: Double
    private val min: Double = numbers.min() ?: 0.0
    private val max: Double = numbers.max() ?: 0.0

    init {
        step = (max - min) / numGroups
    }

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {

        val lower = "%.2f".format((value - 1) * step + min)
        val upper = "%.2f".format((value * step + min))

        return "$lower-$upper"
    }
}