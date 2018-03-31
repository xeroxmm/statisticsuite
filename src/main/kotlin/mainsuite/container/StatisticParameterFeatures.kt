package mainsuite.container

class StatisticParameterFeatures : StatisticParameterBase() {
    private var featureList : ArrayList<Double> = arrayListOf()
    private var absoluteFrequencyList : ArrayList<Double> = arrayListOf()
    private var useStorage : Boolean = false
    private var latestComputationTime : Long = 0

    fun getFeatures(dataList: RawDataList):ArrayList<Double>{
        if( featureList.size > 0 &&
            latestComputationTime > 0 &&
            useStorage &&
            !dataList.hasValueUpdateToBroadcast( this.myIdentifier )
        )
            return featureList

        dataList.registerClientAsUpToDate( this.myIdentifier )

        return this.getNewCreatedFeatureList( dataList.getValues() )
    }
    fun getAbsoluteFrequencies(dataList: RawDataList):ArrayList<Double>{
        if( absoluteFrequencyList.size > 0 &&
                latestComputationTime > 0 &&
                useStorage &&
                !dataList.hasValueUpdateToBroadcast( this.myIdentifier )
        )
            return absoluteFrequencyList

        dataList.registerClientAsUpToDate( this.myIdentifier )

        return this.getNewCreatedFeatureList( dataList.getValues(), 2 )
    }
    private fun getNewCreatedFeatureList(dataList: MutableList<Double>, listToReturn : Int = 1):ArrayList<Double>{
        val tempList : ArrayList<Double> = arrayListOf()
        val tempAbsoluteList : ArrayList<Double> = arrayListOf()
        var tempIndex : Int

        dataList.forEach{
            if(!tempList.contains( it )) {
                tempList.add(it)
                tempAbsoluteList.add( 1.0 )
            } else {
                tempIndex = tempList.indexOf(it)
                tempAbsoluteList.set( tempIndex , tempAbsoluteList[tempIndex].plus(1))
            }
        }

        if(useStorage) {
            this.absoluteFrequencyList = tempAbsoluteList
            this.featureList = tempList
        }

        latestComputationTime = System.currentTimeMillis()

        return when(listToReturn) {
            1 -> tempList
            2 -> tempAbsoluteList
            else -> {
                tempList
            }
        }
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