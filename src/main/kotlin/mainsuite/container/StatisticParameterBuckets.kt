package mainsuite.container

class StatisticParameterBuckets {
    private val parameterList : RawGroupDataParameterList = RawGroupDataParameterList()

    private var lastGroupData: RawGroupDataSheet = RawGroupDataSheet()

    fun useStaticClassSize(isStatic: Boolean) {
        parameterList.useStaticClassSize = isStatic
    }

    fun getGroupedDataFull(dataList: RawDataList): RawGroupDataSheet {
        lastGroupData.doFullCalculation( dataList , parameterList )
        return lastGroupData
    }

    fun setClassThreshold(treshold: Double) {
        parameterList.setClassSize( treshold )
    }
}
