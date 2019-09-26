class Recursive(value: Int) : Val {
    private val _value = value
    override val value: Int
        get() = _value

    private tailrec fun recursivePlus(first: Int, other: Int): Int {
        logging("recursive + ($first, $other)")
        return if (other > 0)
            recursivePlus(first + 1, other - 1)
        else if (other < 0)
            recursivePlus(first - 1, other + 1)
        else
            first
    }

    private tailrec fun recursiveMinus(first: Int, other: Int): Int {
        logging("recursive - ($first, $other)")
        return if (other > 0)
            recursiveMinus(first - 1, other - 1)
        else if (other < 0)
            recursiveMinus(first + 1, other + 1)
        else
            first
    }

    private tailrec fun recursiveTimes(accumulator: Int, first: Int, other: Int): Int {
        logging("recursive * ($accumulator, $first, $other)")
        return if (other < 0)
            recursiveTimes(accumulator, -first, -other)
        else if (other > 1)
            recursiveTimes(recursivePlus(accumulator, first), first, other - 1)
        else
            accumulator
    }

    private tailrec fun recursiveDiv(sign: Int, accumulator: Int, first: Int, other: Int): Int {
        logging("recursive / ($sign, $accumulator, $first, $other)")
        return if (first < 0)
            recursiveDiv(-sign, 0, -first, other)
        else if (other < 0)
            recursiveDiv(-sign, 0, first, -other)
        else if (recursiveMinus(first, other) >= 0)
            recursiveDiv(sign, recursivePlus(accumulator, sign), recursiveMinus(first, other), other)
        else
            accumulator
    }


    override fun plus(other: Val): Val =
        Recursive(recursivePlus(value, other.value))

    override fun minus(other: Val): Val =
        Recursive(recursiveMinus(value, other.value))

    override fun times(other: Val): Val =
        Recursive(recursiveTimes(value, value, other.value))

    override fun div(other: Val): Val =
        Recursive(recursiveDiv(1, 0, value, other.value))

    override fun toString(): String = "$value"
}