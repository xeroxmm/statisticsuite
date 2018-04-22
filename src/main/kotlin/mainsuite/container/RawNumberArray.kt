package mainsuite.container

import mainsuite.interfaces.RawValueCapsule
import java.lang.reflect.Type

class RawNumberArray : RawValueCapsule {
    private var latestComputationTime = 0L
    private var values : ArrayList<Number> = arrayListOf()

    override fun setValue(theNumber: Number) {
        values = arrayListOf( theNumber )
        latestComputationTime = System.currentTimeMillis()
    }

    override fun setList(theList: ArrayList<Number>) {
        values = theList
        latestComputationTime = System.currentTimeMillis()
    }

    override fun getComputationTime(): Long {
        return latestComputationTime
    }

    override fun getValue(): Number {
        return values.first()
    }

    override fun getValueList(): ArrayList<Number> {
        return values
    }

}
