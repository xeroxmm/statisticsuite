package mainsuite.container

class StatisticParameterFeatures : StatisticParameterBase() {
    private var featureList : ArrayList<Number> = arrayListOf()
    private var absoluteFrequencyList : MutableMap<Number, Int> = mutableMapOf()
    private var cumulativeFrequencyList : MutableMap<Number, Double> = mutableMapOf()

    private var useStorage : Boolean = false
    private var latestComputationTime : Long = 0

    fun getFeatures(dataList: RawDataList):ArrayList<Number>{
        if( featureList.size > 0 &&
            latestComputationTime > 0 &&
            useStorage &&
            !dataList.hasValueUpdateToBroadcast( this.myIdentifier )
        )
            return featureList

        dataList.registerClientAsUpToDate( this.myIdentifier )

        this.createFeatureLists( dataList.getValues() )

        return featureList
    }
    fun getRelativeFrequencies(dataList: RawDataList):Map<Number, Double>{
        val tempList : MutableMap<Number, Double> = mutableMapOf()
        this.getAbsoluteFrequencies(dataList).forEach{
            tempList.set( it.key, it.value.toDouble() / dataList.getValues().size )
        }
        return tempList
    }
    fun getAbsoluteFrequencies(dataList: RawDataList):Map<Number, Int>{
        if( absoluteFrequencyList.isNotEmpty() &&
                latestComputationTime > 0 &&
                useStorage &&
                !dataList.hasValueUpdateToBroadcast( this.myIdentifier )
        )
            return absoluteFrequencyList

        dataList.registerClientAsUpToDate( this.myIdentifier )

        this.createFeatureLists( dataList.getValues(), 2 )

        return absoluteFrequencyList
    }
    fun getCumulativeFrequencies(dataList: RawDataList):Map<Number, Double>{
        if( cumulativeFrequencyList.isNotEmpty() &&
                latestComputationTime > 0 &&
                useStorage &&
                !dataList.hasValueUpdateToBroadcast( this.myIdentifier )
        )
            return cumulativeFrequencyList

        dataList.registerClientAsUpToDate( this.myIdentifier )

        this.createFeatureLists( dataList.getValues(), 3 )

        return cumulativeFrequencyList
    }
    private fun createFeatureLists(dataList: MutableList<Number>, listToReturn : Int = 1){
        featureList = arrayListOf()
        absoluteFrequencyList = mutableMapOf()
        cumulativeFrequencyList = mutableMapOf()

        when {
            useStorage -> {
                dataList.forEach {
                    if(!featureList.contains( it )) {
                        featureList.add(it)
                        absoluteFrequencyList.set( it, 1 )
                    } else
                        absoluteFrequencyList.set( it, absoluteFrequencyList[it]!! + 1)
                }
                this.doCurrentFrequencyLoop( dataList.size )
            }
            listToReturn == 1 -> {
                dataList.forEach {
                    if(!featureList.contains( it ))
                        featureList.add( it )
                }
            }
            listToReturn == 2 -> {
                this.doAbsoluteFrequencyLoop( dataList )
            }
            listToReturn == 3 -> {
                this.doAbsoluteFrequencyLoop( dataList )
                this.doCurrentFrequencyLoop( dataList.size )
            }
            else -> throw Exception("Only Values 1... 3 allowed")
        }

        latestComputationTime = System.currentTimeMillis()
    }
    private fun doAbsoluteFrequencyLoop(dataList: MutableList<Number>){
        dataList.forEach {
            if(!absoluteFrequencyList.containsKey( it ) || absoluteFrequencyList[it] == null)
                absoluteFrequencyList.set( it, 1 )
            else
                absoluteFrequencyList.set( it, absoluteFrequencyList[it]!! + 1)
        }
    }
    private fun doCurrentFrequencyLoop(sizeOfList : Int){
        val tempList: List<Number> = absoluteFrequencyList.keys.sortedBy { it.toDouble() }
        var tempSum = 0

        tempList.forEach{
            tempSum += absoluteFrequencyList[it]!!
            cumulativeFrequencyList.set( it, tempSum / sizeOfList.toDouble() )
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