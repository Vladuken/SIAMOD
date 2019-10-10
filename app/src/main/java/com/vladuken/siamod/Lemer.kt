package com.vladuken.siamod

class Lemer(val count : Int = 100000) {

    private fun iterate(a: Double, m: Double, r: Double, numbers: ArrayList<Double>): Double{
        val step1 = a * r
        val step2 = step1 % m
        numbers.add(step2 / m)
        return step2
    }

    fun generate(a: Double, m: Double,rrr : Double) : Collection<Double>{
        val numbers = arrayListOf<Double>()

        var r = rrr
        for(count in 0 until count){
            r = iterate(a,m,r,numbers)
        }

        return numbers
    }

}