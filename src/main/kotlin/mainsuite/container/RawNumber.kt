package mainsuite.container

import mainsuite.interfaces.RawValueCapsule

class RawNumber : RawValueCapsule{
    private var latestComputationTime = 0L
    private var value : Number = 0

    override fun setList(theList: ArrayList<Number>) {
        value = theList.first()
    }

    override fun getValueList(): ArrayList<Number> {
        return arrayListOf( value )
    }

    override fun setValue(theNumber : Number){
        value = theNumber
        latestComputationTime = System.currentTimeMillis()
    }
    override fun getComputationTime():Long {
        return latestComputationTime
    }

    override fun getValue(): Number {
        return value
    }
}