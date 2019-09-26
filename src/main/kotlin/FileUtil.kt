import java.io.File
import java.lang.StringBuilder
import java.util.*

fun fromFile(path: String, encoding: String = "UTF8"): String {
    val sc = Scanner(File(path))
    val sb = StringBuilder()
    var first = true
    while (sc.hasNextLine()) {
        if (first)
            first = false
        else
            sb.appendln()
        sb.append(sc.nextLine())
    }
    return sb.toString()
}

val Char.tex get() = if (this == '\u0000') "$\\lambda$" else "$this"

fun Collection<String>.join(separator: String): String {
    val sb = StringBuilder()
    var first = true
    forEach {
        if (first)
            first = false
        else
            sb.append(separator)
        sb.append(it)
    }
    return sb.toString()
}