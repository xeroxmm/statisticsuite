package mainsuite

import mainsuite.container.RawDataList
import mainsuite.container.StatisticParameterBuckets
import mainsuite.container.StatisticParameterFeatures

class UniDimensionalScale constructor (initialRawDataTable : RawDataList) {
    private val rawDataTable : RawDataList = initialRawDataTable
    private val featureObj: StatisticParameterFeatures = StatisticParameterFeatures()
    private val bucketObj: StatisticParameterBuckets = StatisticParameterBuckets()

    fun getAllFeatures():ArrayList<Number> {
        return featureObj.getFeatures( rawDataTable )
    }
    fun getRelativeFrequency():Map<Number,Double>{
        return featureObj.getRelativeFrequencies( rawDataTable )
    }
    fun getAbsoluteFrequency():Map<Number,Int>{
        return featureObj.getAbsoluteFrequencies( rawDataTable )
    }
    fun getCumulativeFrequency():Map<Number,Double>{
        return featureObj.getCumulativeFrequencies( rawDataTable )
    }
    fun getMedianValue():Double{
        // TODO calculate Median value instead returning zero value
        return 0.0
    }
}