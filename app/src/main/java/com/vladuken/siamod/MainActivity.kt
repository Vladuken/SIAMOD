package com.vladuken.siamod

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.vladuken.siamod.distibutions.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        setContentView(R.layout.activity_main)

        tvA.setText("18435")
        tvM.setText("4895461")
        tvR.setText("45546")

        btnGenerate.setOnClickListener {
            calculateOnViewModel(EmptyDistribution())
        }

        btnGenerateUniform.setOnClickListener {
            try {
                val a = parseEditTextDouble(etUniformA)
                val b = parseEditTextDouble(etUniformB)
                calculateOnViewModel(UniformDistribution(a, b))
            } catch (e: IllegalArgumentException) {
            }
        }

        btnGenerateGauss.setOnClickListener {
            try {
                val m = parseEditTextDouble(etGauss_m)
                val sigma = parseEditTextDouble(etGauss_sigma)
                //TODO add seekbar for N
                calculateOnViewModel(GaussDistribution(m,sigma))
            } catch (e: IllegalArgumentException) {
            }
        }


        btnGenerateExponential.setOnClickListener {
            try {
                val lambda = parseEditTextDouble(etExponential_lambda)
                calculateOnViewModel(Exponential(lambda))
            } catch (e: IllegalArgumentException) {
            }
        }

        btnGenerateGamma.setOnClickListener {
            try {
                val lambda = parseEditTextDouble(etGamma_lambda)
                val n = parseEditTextInt(etGamma_N_int)

                calculateOnViewModel(GammaDistribution(lambda,n))
            } catch (e: IllegalArgumentException) {
            }
        }


        btnGenerateTriangle.setOnClickListener {
            try {
                val a = parseEditTextDouble(etTriangleA)
                val b = parseEditTextDouble(etTriangleB)

                calculateOnViewModel(Triangle(a,b))
            } catch (e: IllegalArgumentException) {
            }
        }

        btnGenerateSimpson.setOnClickListener {
            try {
                val a = parseEditTextDouble(etSimpsonA)
                val b = parseEditTextDouble(etSimpsonB)

                calculateOnViewModel(Simpson(a,b))
            } catch (e: IllegalArgumentException) {
            }
        }






        with(viewModel) {
            randomNumbers.observe(this@MainActivity, Observer {
                setUpBarChart(it, bar_chart, 20)
            })

            expectedValue.observe(this@MainActivity, Observer {
                tvExpectedValue.text = it
            })

            dispersion.observe(this@MainActivity, Observer {
                tvDispersion.text = it
            })

            standartDeviation.observe(this@MainActivity, Observer {
                tvStandartDeviation.text = it
            })

            indirectSigns.observe(this@MainActivity, Observer {
                tvIndirectSigns.text = it
            })

            period.observe(this@MainActivity, Observer {
                if (it == Int.MAX_VALUE) {
                    tvPeriod.text =
                        String.format(getString(R.string.more_than_number), sbAmount.progress)
                } else {
                    tvPeriod.text = it.toString()
                }
            })

            aperiodic.observe(this@MainActivity, Observer {
                tvAperiodic.text = it
            })
        }
    }

    private fun calculateOnViewModel(distribution: Distribution) {
        try {
            val a = parseEditTextInt(tvA)
            val m = parseEditTextInt(tvM)
            val r = parseEditTextInt(tvR)
            val amount = sbAmount.progress
            viewModel.calculateAll(a, m, r, amount, distribution)
        } catch (e: IllegalArgumentException) {
            return
        }
    }

    private fun parseEditTextInt(editText: EditText): Int {
        try {
            return editText.text.toString().toInt()
        } catch (e: Exception) {
            editText.error = getString(R.string.error)
            throw IllegalArgumentException()
        }
    }


    private fun parseEditTextDouble(editText: EditText): Double {
        try {
            return editText.text.toString().toDouble()
        } catch (e: Exception) {
            editText.error = getString(R.string.error)
            throw IllegalArgumentException()
        }
    }


    private fun setUpBarChart(numbers: Collection<Double>, barChart: BarChart, numberOfBars: Int) {
        barChart.clear()

        val groupedList = divideInto(numberOfBars, numbers)

        val entries = arrayListOf<BarEntry>()
        groupedList.forEachIndexed { index, list ->
            entries.add(BarEntry((index + 1).toFloat(), list.size.toFloat()))
        }

        val dataset = BarDataSet(entries, "")
        dataset.stackLabels = groupedList.map {
            it.average().toString()
        }.toTypedArray()
        barChart.data = BarData(dataset)
        barChart.legend.isEnabled = false
        barChart.description.isEnabled = false
        barChart.setFitBars(true)


        val minY = dataset.yMin - numbers.size / numberOfBars
        if (minY < 0)
            barChart.getAxis(YAxis.AxisDependency.LEFT).axisMinimum = 0f
        barChart.getAxis(YAxis.AxisDependency.LEFT).axisMaximum =
            dataset.yMax + numbers.size / numberOfBars

        barChart.xAxis.granularity = 1f
        barChart.xAxis.valueFormatter = RangeXValueFormatter(numbers, numberOfBars)
    }

    private fun divideInto(numGroups: Int, numbers: Collection<Double>): List<List<Double>> {
        if (numbers.isEmpty()) {
            return emptyList()
        }

        val max = numbers.max()!!
        val min = numbers.min()!!
        val step = (max - min) / numGroups
        var acc = min

        val list = mutableListOf<List<Double>>()
        while (acc < max) {
            val intervalList = numbers.filter {
                it >= acc && it < (acc + step)
            }

            list.add(intervalList)
            acc += step
        }

        return list
    }
}
