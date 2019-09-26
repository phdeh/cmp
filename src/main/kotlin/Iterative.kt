
class Iterative(value: Int) : Val {
    private val _value = value
    override val value: Int
        get() = _value

    private fun iterativePlus(first: Int, other: Int): Int {
        logging("iterative + ($first, $other)")
        var other = other
        var accumulator = first
        if (other > 0)
            while (other-- > 0)
                accumulator++
        else if (other < 0)
            while (other++ < 0)
                accumulator--
        return accumulator
    }

    private fun iterativeMinus(first: Int, other: Int): Int {
        logging("iterative - ($first, $other)")
        var other = other
        var accumulator = first
        if (other > 0)
            while (other-- > 0)
                accumulator--
        else if (other < 0)
            while (other++ < 0)
                accumulator++
        return accumulator
    }

    private fun iterativeTimes(first: Int, other: Int): Int {
        logging("iterative * ($first, $other)")
        var other = other
        var accumulator = 0
        if (other > 0)
            while (other-- > 0)
                accumulator = iterativePlus(accumulator, first)
        else if (other < 0)
            while (other++ < 0)
                accumulator = iterativeMinus(accumulator, first)
        return accumulator
    }

    private fun iterativeDiv(first: Int, other: Int): Int {
        logging("iterative / ($first, $other)")
        var f = kotlin.math.abs(first)
        val o = kotlin.math.abs(other)
        var accumulator = 0
        while (iterativeMinus(f, o) >= 0) {
            f = iterativeMinus(f, o)
            accumulator++
        }
        return iterativeTimes(accumulator, if ((first > 0) xor (other > 0)) -1 else 1)
    }


    override fun plus(other: Val): Val =
        Iterative(iterativePlus(value, other.value))

    override fun minus(other: Val): Val =
        Iterative(iterativeMinus(value, other.value))

    override fun times(other: Val): Val =
        Iterative(iterativeTimes(value, other.value))

    override fun div(other: Val): Val =
        Iterative(iterativeDiv(value, other.value))

    override fun toString(): String = "$value"
}