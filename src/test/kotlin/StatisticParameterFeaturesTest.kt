import mainsuite.container.RawDataList
import mainsuite.container.StatisticParameterFeatures
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class StatisticParameterFeaturesTest {
    private val knownArrayListSigned: ArrayList<Double> = arrayListOf(-5.5, -4.4, -3.3, -2.2, -1.1, 0.0, 1.1, 2.2, 3.3, 4.4, 5.5)
    private val knownArrayListUnsigned: ArrayList<Double> = arrayListOf(5.5, 4.4, 3.3, 2.2, 1.1, 0.0, 1.1, 2.2, 3.3, 4.4, 5.5)

    @Test
    fun isStorageWorkingT_1() {
        val rawDataObj = RawDataList()
        rawDataObj.addValues(ArrayList<Double>(knownArrayListSigned))

        val paramObj = StatisticParameterFeatures()
        paramObj.useStorage(true)

        paramObj.getFeatures(rawDataObj)

        val lastTime: Long = paramObj.getComputationTime()
        var i = 10
        while (i > 0) {
            i--
            Thread.sleep( 200 )
            paramObj.getFeatures(rawDataObj)
        }

        assertEquals(lastTime, paramObj.getComputationTime())
    }

    @Test
    fun isStorageWorkingT_2() {
        val rawDataObj = RawDataList()
        rawDataObj.addValues(ArrayList<Double>(knownArrayListSigned))

        val paramObj = StatisticParameterFeatures()
        paramObj.useStorage(true)

        paramObj.getFeatures(rawDataObj)
        val lastTime_1: Long = paramObj.getComputationTime() // is != 0

        paramObj.getFeatures(rawDataObj)
        val lastTime_2: Long = paramObj.getComputationTime()   // is != 0 && == lastTime_1

        paramObj.useStorage(false)
        val lastTime_3 = paramObj.getComputationTime() // is zero

        paramObj.getFeatures(rawDataObj)
        val lastTime_4 = paramObj.getComputationTime() // is != 0

        assertEquals(
                lastTime_1 != 0L &&
                        lastTime_1 == lastTime_2 &&
                        lastTime_3 == 0L &&
                        lastTime_4 > 0
                , true)
    }

    @Test
    fun getFeaturesSignedUntouchedNoStorage() {
        val rawDataObj = RawDataList()
        rawDataObj.addValues(ArrayList<Double>(knownArrayListSigned))

        val paramObj = StatisticParameterFeatures()

        assertEquals(paramObj.getFeatures(rawDataObj).sort(), knownArrayListSigned.sort())
    }

    @Test
    fun getFeaturesUnsignedUntouchedNoStorage() {
        val rawDataObj = RawDataList()
        rawDataObj.addValues(ArrayList<Double>(knownArrayListUnsigned))

        val testArrayList: ArrayList<Double> = arrayListOf(5.5, 4.4, 3.3, 2.2, 1.1, 0.0)

        val paramObj = StatisticParameterFeatures()

        assertEquals(testArrayList.sort(), paramObj.getFeatures(rawDataObj).sort())
    }

    @Test
    fun getFeaturesUnsignedTouchedNoStorage() {
        val rawDataObj = RawDataList()
        rawDataObj.addValues(ArrayList<Double>(knownArrayListUnsigned))

        val testArrayList: ArrayList<Double> = arrayListOf(-3.0, 5.5, 4.4, 3.3, 2.2, 1.1, 0.0, 3.0)

        val paramObj = StatisticParameterFeatures()
        paramObj.getFeatures( rawDataObj )

        rawDataObj.addValue( 3 )
        rawDataObj.addValue( -3 )

        assertEquals(testArrayList.sort(), paramObj.getFeatures(rawDataObj).sort())
    }

    @Test
    fun getFeaturesUnsignedTouchedStorage() {
        val rawDataObj = RawDataList()
        rawDataObj.addValues(ArrayList<Double>(knownArrayListUnsigned))

        val testArrayList: ArrayList<Double> = arrayListOf(-3.0, 5.5, 4.4, 3.3, 2.2, 1.1, 0.0, 3.0)

        val paramObj = StatisticParameterFeatures()
        paramObj.useStorage( true )

        paramObj.getFeatures( rawDataObj )
        var lastTime = paramObj.getComputationTime()

        rawDataObj.addValue( 3 )
        rawDataObj.addValue( -3 )

        Thread.sleep(200)
        paramObj.getFeatures(rawDataObj)

        if(paramObj.getComputationTime() == lastTime)
            assertNotEquals(lastTime, paramObj.getComputationTime())

        Thread.sleep(200)
        paramObj.getFeatures(rawDataObj)
        lastTime = paramObj.getComputationTime()
        if(lastTime != paramObj.getComputationTime())
            assertEquals(lastTime, paramObj.getComputationTime())
        else
            assertEquals(testArrayList.sort(), paramObj.getFeatures(rawDataObj).sort())
    }

    @Test
    fun isAbsoluteFrequencyUntouchedNoStorage(){
        val rawDataObj = RawDataList()
        rawDataObj.addValues(ArrayList<Double>(knownArrayListUnsigned))

        val paramObj = StatisticParameterFeatures()

        assertEquals(paramObj.getAbsoluteFrequencies(rawDataObj).sort(), arrayListOf(2.0,2.0,2.0,2.0,1.0).sort())
    }

    @Test
    fun isAbsoluteFrequencyTouchedNoStorage(){
        val rawDataObj = RawDataList()
        rawDataObj.addValues(ArrayList<Double>(knownArrayListUnsigned))

        val paramObj = StatisticParameterFeatures()
        paramObj.getAbsoluteFrequencies(rawDataObj)

        rawDataObj.addValue(5.5)

        assertEquals(paramObj.getAbsoluteFrequencies(rawDataObj).sort(), arrayListOf(3.0,2.0,2.0,2.0,1.0).sort())
    }

    @Test
    fun isAbsoluteFrequencyTouchedStorage(){
        val rawDataObj = RawDataList()
        rawDataObj.addValues(ArrayList<Double>(knownArrayListUnsigned))

        val paramObj = StatisticParameterFeatures()
        paramObj.useStorage( true )
        paramObj.getAbsoluteFrequencies(rawDataObj)

        rawDataObj.addValue(5.5)

        paramObj.getFeatures( rawDataObj )
        val timeToTest = paramObj.getComputationTime()
        Thread.sleep(200)
        paramObj.getAbsoluteFrequencies(rawDataObj)

        assertEquals(timeToTest, paramObj.getComputationTime())
    }
}