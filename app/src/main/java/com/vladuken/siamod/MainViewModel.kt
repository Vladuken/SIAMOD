package com.vladuken.siamod

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vladuken.siamod.distibutions.Distribution
import com.vladuken.siamod.distibutions.EmptyDistribution
import kotlin.math.sqrt

class MainViewModel : ViewModel() {

    val randomNumbers = MutableLiveData<Collection<Double>>()

    val expectedValue = MutableLiveData<String>()
    val dispersion = MutableLiveData<String>()
    val standartDeviation = MutableLiveData<String>()
    val indirectSigns = MutableLiveData<String>()
    val period = MutableLiveData<Int>()
    val aperiodic = MutableLiveData<String>()

    fun calculateAll(
        a: Int,
        m: Int,
        r: Int,
        amount: Int,
        distribution: Distribution = EmptyDistribution()
    ) {
        //Часть 1
        var listRandomNumbers =
            Lemer(amount).generate(a.toDouble(), m.toDouble(), r.toDouble())
        listRandomNumbers = distribution.transform(listRandomNumbers)

        randomNumbers.postValue(listRandomNumbers)

        //Часть 2
        //Медиана
        val M = calculateMedian(listRandomNumbers)
        //Дисперсия
        val D = calculateDispersion(listRandomNumbers, M)
        //Среднеквадратичное отклонение
        val _standartDeviation = sqrt(D)
        //косвенные признаки
        val _indirectSigns = calculateIndirectSigns(listRandomNumbers)

        //part 4
        val _period = calculatePeriod(listRandomNumbers, listRandomNumbers.last())
        val _aperiodic = calculateAperiodic(listRandomNumbers, _period)


        val format = "%.9f"
        expectedValue.value = format.format(M)
        dispersion.value = format.format(D)
        standartDeviation.value = format.format(_standartDeviation)
        indirectSigns.value = format.format(_indirectSigns)

        period.value = _period

        aperiodic.value = _aperiodic.toString()
    }


    private fun calculateMedian(numbers: Collection<Double>): Double {
        return numbers.average()
    }

    private fun calculateDispersion(numbers: Collection<Double>, median: Double): Double {
        return numbers.fold(0.0) { acc, d ->
            acc + (d - median) * (d - median)
        } / numbers.size
    }

    private fun calculateIndirectSigns(numbers: Collection<Double>): Double {
        val points = devideListIntoPairs(numbers)

        return points.fold(0.0) { acc, pair ->
            val (x1, x2) = pair

            val point = x1 * x1 + x2 * x2
            if (point < 1) {
                acc + 1
            } else {
                acc
            }
        } / points.size

    }

    private fun <T> devideListIntoPairs(numbers: Collection<T>): List<Pair<T, T>> {
        return numbers.zipWithNext().filterIndexed { index, pair -> index % 2 == 0 }
    }

    private fun <T> calculatePeriod(numbers: Collection<T>, elementToCompare: T): Int {
        val pair = numbers.mapIndexed { index, element ->
            Pair(index, element)
        }.filter {
            it.second == elementToCompare
        }.map {
            it.first
        }.take(2)

        if (pair.size == 2) {
            return pair[1] - pair[0]
        }

        return Int.MAX_VALUE
    }

    private fun <T> calculateAperiodic(items: Collection<T>, period: Int): Int {
        return items.size.rem(period)
    }
}