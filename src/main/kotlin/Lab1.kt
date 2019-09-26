fun logging(any: Any) {
    // println(any)
}

fun main() {
    //*
    val x = readLine()?.toInt()
    val y = readLine()?.toInt()
    if (x == null)
        return
    if (y == null)
        return
    // */ val x = 256; val y = 4
    println(Recursive(x) / Recursive(y))
    println(Iterative(x) / Iterative(y))
}