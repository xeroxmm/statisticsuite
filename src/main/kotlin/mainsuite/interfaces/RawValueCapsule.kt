package mainsuite.interfaces

import java.lang.reflect.Type

interface RawValueCapsule {
    fun setValue(theNumber : Number)
    fun setList(theList : ArrayList<Number>)
    fun getComputationTime():Long
    fun getValue(): Number
    fun getValueList(): ArrayList<Number>
}