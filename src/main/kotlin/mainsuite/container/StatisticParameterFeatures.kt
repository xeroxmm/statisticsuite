package mainsuite.container

class StatisticParameterFeatures : StatisticParameterBase() {
    private var featureList : ArrayList<Double> = arrayListOf()
    private var useStorage : Boolean = false
    private var latestComputationTime : Long = 0

    fun getFeatures(dataList: RawDataList):ArrayList<Double>{
        if(latestComputationTime > 0 && useStorage && !dataList.hasValueUpdateToBroadcast( this.myIdentifier ))
            return featureList

        dataList.registerClientAsUpToDate( this.myIdentifier )

        return this.getNewCreatedFeatureList( dataList.getValues() )
    }
    private fun getNewCreatedFeatureList(dataList: MutableList<Double>):ArrayList<Double>{
        val tempList : ArrayList<Double> = arrayListOf()

        dataList.forEach{
            if(!tempList.contains( it ))
                tempList.add( it )
        }

        if(useStorage) {
            this.featureList = tempList
        }

        latestComputationTime = System.currentTimeMillis()

        return tempList
    }
    fun useStorage(use:Boolean){
        if(use != useStorage)
            latestComputationTime = 0

        useStorage = use
    }
    fun getComputationTime(): Long{
        return latestComputationTime
    }
}