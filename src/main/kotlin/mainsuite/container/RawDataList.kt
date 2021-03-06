package mainsuite.container

import mainsuite.interfaces.OnChangeBroadcastInterface

class RawDataList {
    private var rawData: ArrayList<Number> = arrayListOf()
    private var rawDataSorted: ArrayList<Number> = arrayListOf()
    private val clientsToPropagateOnChange: ArrayList<OnChangeBroadcastInterface> = arrayListOf()
    private val clientsWithCurrentData: ArrayList<String> = arrayListOf()

    fun useAsBaseList(listOfValues: ArrayList<Number>) {
        rawData = listOfValues
        broadcastChangeToClients()
    }

    fun addValues(listOfValues: ArrayList<Number>) {
        listOfValues.forEach {
            rawData.add(it)
        }
        broadcastChangeToClients()
    }

    fun addValue(additionalValue: Number) {
        rawData.add(additionalValue)
        broadcastChangeToClients()
    }

    fun addBroadcastClient(client: OnChangeBroadcastInterface) {
        clientsToPropagateOnChange.add(client)
    }

    fun isRawDataEqual(rawDataObj: RawDataList): Boolean {
        if (this.rawData.size != rawDataObj.getValues().size)
            return false
        this.rawData.forEachIndexed { key, dbl ->
            if (rawDataObj.getValues()[key] != dbl) return false
        }

        return true
    }

    fun getValues(): ArrayList<Number> {
        return rawData
    }

    fun hasValueUpdateToBroadcast(clientIdentifier: String): Boolean {
        return !clientsWithCurrentData.contains(clientIdentifier)
    }

    fun registerClientAsUpToDate(clientIdentifier: String) {
        if (hasValueUpdateToBroadcast(clientIdentifier))
            clientsWithCurrentData.add(clientIdentifier)
    }

    fun getValuesSortedASC(): ArrayList<Number> {
        if (rawDataSorted.isEmpty()) {
            rawDataSorted = ArrayList<Number>(rawData)
            rawDataSorted.sortBy {
                it.toDouble()
            }
        }
        return rawDataSorted
    }

    private fun broadcastChangeToClients() {
        rawDataSorted.clear()

        clientsToPropagateOnChange.forEach {
            it.receiveOnChangeBroadcast()
        }
        this.clientsWithCurrentData.clear()
    }

    fun getValue(i: Int): Number {
        return when {
            rawData.size > 0 -> rawData[0]
            else -> {
                Double.NaN
            }
        }
    }

    fun getSize(): Int {
        return rawData.size
    }
}
