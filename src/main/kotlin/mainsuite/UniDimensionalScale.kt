package mainsuite

import mainsuite.container.RawDataList
import mainsuite.container.StatisticParameterFeatures

class UniDimensionalScale() {
    private val rawDataTable : RawDataList = RawDataList()
    private val featureObj: StatisticParameterFeatures = StatisticParameterFeatures()

    fun getAllFeatures():ArrayList<Double> {
        return featureObj.getFeatures( rawDataTable )
    }
}