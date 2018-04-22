package mainsuite.container

import java.io.Console

class StatisticParameterLocation : StatisticParameterBase() {
    private var latestMedian : RawNumber = RawNumber()
    private var latestArithmeticMean : RawNumber = RawNumber()

    fun getMedian():Number {
        val list = latestDataList.getValuesSortedASC()
        val listSize = list.size

        return when {
            listSize == 0 -> Double.NaN
            listSize % 2 == 1 -> list[(listSize-1)/2]
            else -> (list[listSize/2].toDouble() + list[listSize/2-1].toDouble()) / 2
        }

    }
    fun getMedian(rawDataObj: RawDataList):Number {
        latestDataList = rawDataObj
        return getMedian()
    }

    fun getArithmeticMean(): Number {
        if(this.isUpdateNeeded( latestArithmeticMean )){
            calcArithmeticMean()
            this.registerClientAsUpToDate()
        }
        return latestArithmeticMean.getValue()
    }

    private fun calcArithmeticMean() {
        var sum = 0.0
        latestDataList.getValues().forEach {
            sum += it.toDouble()
        }
        latestArithmeticMean.setValue(sum / latestDataList.getSize())
    }

    fun getArithmeticMean(rawDataObj: RawDataList): Number {
        latestDataList = rawDataObj
        return getArithmeticMean()
    }
}