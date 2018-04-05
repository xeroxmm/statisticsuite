package mainsuite.container

class RawGroupDataParameterList {
    private var classSize : Double = 0.025
    var useStaticClassSize : Boolean = true

    fun setClassSize(threshold: Double) {
        classSize = if (threshold < 0) 0.0 else if (threshold > 1) 1.0 else threshold
    }
}
