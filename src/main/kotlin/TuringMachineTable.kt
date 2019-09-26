import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileInputStream
import java.io.File

class TuringMachineTable(file: String) {
    enum class Direction(val shiftRight: Int, val title: String = "???", val tex: String = "???") {
        NOT_STATED(0),
        LEFT(-1, "<–", "\\leftarrow"),
        RIGHT(1, "–>", "\\rightarrow")
    }

    enum class Action {
        NONE, HALT, FINALE
    }

    inner class Entry(
        val action: Action,
        val direction: Direction,
        val character: Char,
        val state: Int
    ) {
        override fun toString(): String =
            when (action) {
                Action.HALT -> "HALT"
                Action.FINALE -> "FINALE"
                else -> "${direction.title}, \"${character.tex}\", Q$state"
            }

    }

    private val entries: Map<Pair<Char, Int>, Entry>
    private val states: IntRange
    private val alphabet: List<Char>

    init {
        operator fun Regex.contains(text: CharSequence): Boolean = this.matches(text)
        val entries = mutableMapOf<Pair<Char, Int>, Entry>()

        val myFile = File("turing/$file.xlsx")
        val fis = FileInputStream(myFile)
        val myWorkBook = WorkbookFactory.create(fis)
        val mySheet = myWorkBook.getSheetAt(0)
        val rowIterator = mySheet.iterator()
        var firstRow = true
        var lastState = 0
        var alphabet = mutableSetOf<Char>()
        row@ while (rowIterator.hasNext()) {
            val row = rowIterator.next()
            if (firstRow) {
                firstRow = false
                continue@row
            }
            var firstColumn = true
            val cellIterator = row.cellIterator()
            var rowCharacter = Tape.EMPTY_SYMBOL
            var columnIndex = -2
            column@ while (cellIterator.hasNext()) {
                columnIndex++
                val cell = cellIterator.next()
                val celltext: String
                when (cell.cellType) {
                    Cell.CELL_TYPE_STRING -> celltext = cell.stringCellValue
                    Cell.CELL_TYPE_NUMERIC -> celltext = cell.numericCellValue.toString()
                    Cell.CELL_TYPE_BOOLEAN -> celltext = cell.booleanCellValue.toString()
                    else -> celltext = ""
                }
                if (firstColumn) {
                    firstColumn = false
                    if (celltext.trim() != "λ")
                        rowCharacter = celltext.first()
                    else
                        rowCharacter = Tape.EMPTY_SYMBOL
                    continue@column
                }
                val args = celltext.split(",")

                var action = Action.NONE
                var direction = Direction.NOT_STATED
                var character = rowCharacter
                var state = cell.columnIndex - 1
                lastState = maxOf(lastState, state)
                alphabet.add(rowCharacter)
                args.forEach {
                    val str = it.trim()
                    when (it) {
                        "–>" -> direction = Direction.RIGHT
                        "<–" -> direction = Direction.LEFT
                        "FINALE" -> action = Action.FINALE
                        "HALT" -> action = Action.HALT
                        else -> {
                            if (str.startsWith("\"")) {
                                if (str[1] != 'λ')
                                    character = str[1]
                                else
                                    character = Tape.EMPTY_SYMBOL
                            } else if (str.startsWith("Q")) {
                                state = str.substring(1).toInt()
                            }
                        }
                    }
                }

                entries[rowCharacter to columnIndex] = Entry(
                    action,
                    direction,
                    character,
                    state
                )
            }
        }

        states = 0..lastState
        this.alphabet = alphabet.toList()

        this.entries = entries.toMap()
    }

    fun toLaTeX(): String {
        val sb = StringBuilder()
        sb.append(
            "\\begin{table}[H]\n" +
                    "\\begin{center}\n" +
                    "\\begin{tabular}{c|"
        )
        for (i in states)
            sb.append('c')
        sb.appendln(
            "}\n" +
                    "\\toprule"
        )
        for (i in states) {
            sb.append("&\$Q_{$i}$")
        }
        sb.appendln("\\\\ \\midrule")
        for (c in alphabet) {
            sb.append("«${c.tex}»")
            for (i in states) {
                sb.append('&')
                val s = this[c, i]
                if (s.action == Action.NONE) {
                    val sbb = mutableListOf<String>()
                    sbb.add("\$${s.direction.tex}\$")
                    if (c != s.character)
                        sbb.add("«${s.character.tex}»")
                    if (i != s.state)
                        sbb.add("\$Q_{${s.state}}\$")
                    sb.append("\\thead{${sbb.joinToString(", ")}}")
                } else if (s.action == Action.FINALE) {
                    sb.append("\\textit{конец}")
                }
            }
            sb.appendln("\\\\")
        }
        sb.append(
            "\\bottomrule\n" +
                    "\\end{tabular}\n" +
                    "\\end{center}\n" +
                    "\\end{table}"
        )
        return sb.toString()
    }

    operator fun get(character: Char, state: Int) = entries[character to state] ?: Entry(
        Action.HALT, Direction.NOT_STATED, character, state
    )

}