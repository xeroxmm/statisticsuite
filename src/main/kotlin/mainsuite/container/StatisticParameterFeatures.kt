package mainsuite.container

class StatisticParameterFeatures : StatisticParameterBase() {
    private var featureList: ArrayList<Number> = arrayListOf()
    private var absoluteFrequencyList: MutableMap<Number, Int> = mutableMapOf()
    private var cumulativeFrequencyList: MutableMap<Number, Double> = mutableMapOf()

    private var latestMode : RawNumberArray = RawNumberArray()

    private var latestComputationTime: Long = 0

    override fun useStorage(use: Boolean){
        if(this.useStorage != use)
            latestComputationTime = 0L

        this.useStorage = use
    }

    fun getFeatures(): ArrayList<Number>{
        if (featureList.size > 0 &&
                latestComputationTime > 0 &&
                useStorage &&
                !latestDataList.hasValueUpdateToBroadcast(this.myIdentifier)
        )
            return featureList

        this.registerClientAsUpToDate()

        this.createFeatureLists(latestDataList.getValues())

        return featureList
    }
    fun getFeatures(dataList: RawDataList): ArrayList<Number> {
        latestDataList = dataList
        return getFeatures()
    }

    fun getRelativeFrequencies(dataList: RawDataList): Map<Number, Double> {
        val tempList: MutableMap<Number, Double> = mutableMapOf()
        this.getAbsoluteFrequencies(dataList).forEach {
            tempList.set(it.key, it.value.toDouble() / dataList.getValues().size)
        }
        return tempList
    }

    fun getAbsoluteFrequencies(dataList: RawDataList): Map<Number, Int> {
        if (absoluteFrequencyList.isNotEmpty() &&
                latestComputationTime > 0 &&
                useStorage &&
                !dataList.hasValueUpdateToBroadcast(this.myIdentifier)
        )
            return absoluteFrequencyList

        dataList.registerClientAsUpToDate(this.myIdentifier)

        this.createFeatureLists(dataList.getValues(), 2)

        return absoluteFrequencyList
    }

    fun getCumulativeFrequencies(dataList: RawDataList): Map<Number, Double> {
        if (cumulativeFrequencyList.isNotEmpty() &&
                latestComputationTime > 0 &&
                useStorage &&
                !dataList.hasValueUpdateToBroadcast(this.myIdentifier)
        )
            return cumulativeFrequencyList

        dataList.registerClientAsUpToDate(this.myIdentifier)

        this.createFeatureLists(dataList.getValues(), 3)

        return cumulativeFrequencyList
    }

    private fun createFeatureLists(dataList: MutableList<Number>, listToReturn: Int = 1) {
        featureList = arrayListOf()
        absoluteFrequencyList = mutableMapOf()
        cumulativeFrequencyList = mutableMapOf()

        when {
            useStorage -> {
                dataList.forEach {
                    if (!featureList.contains(it)) {
                        featureList.add(it)
                        absoluteFrequencyList.set(it, 1)
                    } else
                        absoluteFrequencyList.set(it, absoluteFrequencyList[it]!! + 1)
                }
                this.doCurrentFrequencyLoop(dataList.size)
            }
            listToReturn == 1 -> {
                dataList.forEach {
                    if (!featureList.contains(it))
                        featureList.add(it)
                }
            }
            listToReturn == 2 -> {
                this.doAbsoluteFrequencyLoop(dataList)
            }
            listToReturn == 3 -> {
                this.doAbsoluteFrequencyLoop(dataList)
                this.doCurrentFrequencyLoop(dataList.size)
            }
            else -> throw Exception("Only Values 1... 3 allowed")
        }

        latestComputationTime = System.currentTimeMillis()
    }

    private fun doAbsoluteFrequencyLoop(dataList: MutableList<Number>) {
        dataList.forEach {
            if (!absoluteFrequencyList.containsKey(it) || absoluteFrequencyList[it] == null)
                absoluteFrequencyList.set(it, 1)
            else
                absoluteFrequencyList.set(it, absoluteFrequencyList[it]!! + 1)
        }
    }

    private fun doCurrentFrequencyLoop(sizeOfList: Int) {
        val tempList: List<Number> = absoluteFrequencyList.keys.sortedBy { it.toDouble() }
        var tempSum = 0

        tempList.forEach {
            tempSum += absoluteFrequencyList[it]!!
            cumulativeFrequencyList.set(it, tempSum / sizeOfList.toDouble())
        }
    }
    fun getComputationTime(): Long {
        return latestComputationTime
    }

    fun getMode():ArrayList<Number>{
        if(this.isUpdateNeeded( latestMode)){
            calcMode()
            this.registerClientAsUpToDate()
        }
        return latestMode.getValueList()
    }

    private fun calcMode() {
        var list = getAbsoluteFrequencies( latestDataList )
        var maxAmount = 0
        var maxValue : ArrayList<Number> = arrayListOf()
        list.forEach {
            if(it.value > maxAmount){
                maxValue = arrayListOf(it.key)
                maxAmount = it.value
            } else if(it.value == maxAmount)
                maxValue.add(it.key)
        }

        when (maxAmount) {
            0 -> latestMode.setValue(Double.NaN)
            else -> { latestMode.setList( maxValue ) }
        }
    }

    fun getMode(rawDataObj: RawDataList): ArrayList<Number> {
        latestDataList = rawDataObj
        return getMode()
    }
}