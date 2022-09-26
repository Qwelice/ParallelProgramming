package calculations.vectors

import calculations.parallel.Parallel
import java.lang.IllegalArgumentException
import java.util.Random

class VectorNumeric (val size: Int){
    private val data = Array(size){0.0}

    operator fun get(index: Int) : Double = data[index]
    operator fun set(index: Int, value: Double){
        data[index] = value
    }

    constructor(inputData: Array<Double>) : this(inputData.size){
        repeat(inputData.size){
            this[it] = inputData[it]
        }
    }

    constructor(inputData: List<Double>) : this(inputData.size){
        repeat(inputData.size){
            this[it] = inputData[it]
        }
    }

    constructor(length: Int, initializer: (Int) -> Double) : this(length){
        repeat(length){
            this[it] = initializer(it)
        }
    }

    operator fun plus(other: VectorNumeric) : VectorNumeric{
        if(size != other.size) throw IllegalArgumentException("sizes are not equal")
        return VectorNumeric(size).let {that ->
            repeat(size){
                that[it] = this[it] + other[it]
            }
            that
        }
    }

    fun parallelPlus(other: VectorNumeric) : VectorNumeric{
        if(size != other.size) throw IllegalArgumentException("sizes are not equal")
        TODO("Реализовать параллельное сложение векторов")
    }

    operator fun minus(other: VectorNumeric) : VectorNumeric{
        if(size != other.size) throw IllegalArgumentException("sizes are not equal")
        return VectorNumeric(size).apply {
            repeat(size){
                this[it] = this@VectorNumeric[it] - other[it]
            }
        }
    }

    override fun toString(): String = StringBuilder().apply {
        repeat(size){
            append("${this@VectorNumeric[it]} ")
        }
    }.toString().trim()

    companion object{
        private val random = Random()

        fun zeros(length: Int) : VectorNumeric = VectorNumeric(length)

        fun ones(length: Int) : VectorNumeric = VectorNumeric(length).apply {
            repeat(length){
                this[it] = 1.0
            }
        }

        fun randomInt(startInclusive: Int, endExclusive: Int, length: Int) : VectorNumeric = VectorNumeric(length).apply {
            repeat(length){
                this[it] = random.nextInt(startInclusive, endExclusive).toDouble()
            }
        }

        fun randomDouble(length: Int) : VectorNumeric = VectorNumeric(length).apply {
            repeat(length){
                this[it] = random.nextDouble()
            }
        }
    }
}