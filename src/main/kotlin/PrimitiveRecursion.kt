tailrec fun sum(x: Int, y: Int): Int = when (y) {
    0 -> x
    else -> sum(x, y - 1)
}

tailrec fun product(x: Int, y: Int, accumulator: Int = 0): Int = when (y) {
    0 -> accumulator
    else -> product(x, y - 1, sum(accumulator, x))
}
