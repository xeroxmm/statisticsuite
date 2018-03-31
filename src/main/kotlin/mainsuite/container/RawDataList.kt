package mainsuite.container

import mainsuite.interfaces.OnChangeBroadcastInterface

class RawDataList {
    private var rawData: ArrayList<Double> = arrayListOf()
    private val clientsToPropagateOnChange : ArrayList<OnChangeBroadcastInterface> = arrayListOf()
    private val clientsWithCurrentData : ArrayList<String> = arrayListOf()

    fun useAsBaseList(listOfValues: ArrayList<Double>){
        rawData = listOfValues
        broadcastChangeToClients()
    }
    fun addValues(listOfValues: ArrayList<Double>){
        listOfValues.forEach {
            rawData.add( it )
        }
        broadcastChangeToClients()
    }
    fun addValues(listOfValues: Array<Number>){
        listOfValues.forEach {
            rawData.add(it.toDouble())
        }
        broadcastChangeToClients()
    }

    fun addValue(additionalValue: Number){
        rawData.add( additionalValue.toDouble() )
        broadcastChangeToClients()
    }

    fun addBroadcastClient(client : OnChangeBroadcastInterface){
        clientsToPropagateOnChange.add( client )
    }

    fun isRawDataEqual( rawDataObj : RawDataList ) : Boolean {
        if(this.rawData.size != rawDataObj.getValues().size)
            return false
        this.rawData.forEachIndexed{
            key, dbl -> if(rawDataObj.getValues()[key] != dbl) return false
        }

        return true
    }
    fun getValues():ArrayList<Double> { return rawData }
    fun hasValueUpdateToBroadcast(clientIdentifier : String) : Boolean {
        return !clientsWithCurrentData.contains( clientIdentifier )
    }
    fun registerClientAsUpToDate(clientIdentifier : String){
        if(hasValueUpdateToBroadcast( clientIdentifier ))
            clientsWithCurrentData.add( clientIdentifier )
    }

    private fun broadcastChangeToClients(){
        clientsToPropagateOnChange.forEach {
            it.receiveOnChangeBroadcast()
        }
        this.clientsWithCurrentData.clear()
    }
}
