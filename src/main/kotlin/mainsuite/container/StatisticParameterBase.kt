package mainsuite.container

import java.util.*

open class StatisticParameterBase {
    protected val myIdentifier : String = "SPI" + System.currentTimeMillis() + "-" + (Random().nextInt(8990000) + 1000000)
}