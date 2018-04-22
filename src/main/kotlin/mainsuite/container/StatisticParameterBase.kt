package mainsuite.container

import mainsuite.interfaces.RawValueCapsule
import java.util.*

open class StatisticParameterBase {
    protected val myIdentifier : String = "SPI" + System.currentTimeMillis() + "-" + (Random().nextInt(8990000) + 1000000)
    protected var latestDataList: RawDataList = RawDataList()
    protected var useStorage: Boolean = false

    protected fun isUpdateNeeded(rawNumber : RawValueCapsule): Boolean {
        return rawNumber.getComputationTime() == 0L ||
                !useStorage ||
                latestDataList.hasValueUpdateToBroadcast(this.myIdentifier)
    }
    protected fun registerClientAsUpToDate(){
        latestDataList.registerClientAsUpToDate( myIdentifier )
    }

    open fun useStorage(use: Boolean) {
        useStorage = use
    }
}