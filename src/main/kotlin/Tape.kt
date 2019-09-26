import kotlin.math.abs

class Tape(line: String = "") {
    companion object {
        const val EMPTY_SYMBOL = '\u0000'
    }

    private var caret = 0
    private val map = HashMap<Int, Char>()

    init {
        line.forEachIndexed { index, c ->
            map[index] = c
        }
    }

    var current: Char
        get() = map.getOrDefault(caret, EMPTY_SYMBOL)
        set(value) {
            if (value == EMPTY_SYMBOL)
                map.remove(caret)
            else
                map[caret] = value
        }

    fun right() {
        caret++
    }

    fun left() {
        caret--
    }

    fun shift(direction: Int) {
        if (direction != 0)
            caret += direction / abs(direction)
    }

    private val min
        get() = (minOf(caret, (map.filter { it.value != ' ' && it.value != EMPTY_SYMBOL }.keys.min() ?: 0)))
    private val max
        get() = (maxOf(caret, (map.filter { it.value != ' ' && it.value != EMPTY_SYMBOL }.keys.max() ?: 0)))

    fun toFancyString(): String {
        val sb = StringBuilder()
        (min..max).forEach {
            sb.append(map.getOrDefault(it, ' '))
        }
        sb.appendln()
        (min..max).forEach {
            if (it == caret)
                sb.append('^')
            else
                sb.append(' ')
        }
        return sb.toString()
    }

    override fun toString(): String {
        val sb = StringBuilder()
        (min..max).forEach {
            sb.append(map.getOrDefault(it, ' '))
        }
        return sb.toString()
    }
}