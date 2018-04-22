import mainsuite.container.RawDataList
import mainsuite.interfaces.OnChangeBroadcastInterface
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


class RawDataListTest {
    private val knownArrayListUnsigned: ArrayList<Double> = arrayListOf(5.5, 4.4, 3.3, 2.2, 1.1, 0.0, 1.1, 2.2, 3.3, 4.4, 5.5)

    @Test
    fun checkBaseListRewrite() {
        val rawObj = RawDataList()
        rawObj.useAsBaseList( ArrayList<Number>(knownArrayListUnsigned) )

        Assertions.assertEquals(knownArrayListUnsigned, rawObj.getValues())
    }

    @Test
    fun checkChangeInfoPush(){
        class TestOnChangeBroadcast : OnChangeBroadcastInterface {
            var gotCalled : Boolean = false
            override fun receiveOnChangeBroadcast() {
                gotCalled = true
            }
        }

        val testClass = TestOnChangeBroadcast()
        val rawObj = RawDataList()
        rawObj.addBroadcastClient( testClass )
        rawObj.addValue( 5 )

        assertEquals(true, testClass.gotCalled )
    }
}