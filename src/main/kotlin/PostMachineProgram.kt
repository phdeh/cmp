import javax.swing.RowFilter

class PostMachineProgram(path: String) {
    abstract class Command(val jumpTo: Int, val string: () -> String, val jump: Boolean = true)
    class Mark(jumpTo: Int) : Command(jumpTo, { "1\t$jumpTo" })
    class Unmark(jumpTo: Int) : Command(jumpTo, { "0\t$jumpTo" })
    class Left(jumpTo: Int) : Command(jumpTo, { "<\t$jumpTo" })
    class Right(jumpTo: Int) : Command(jumpTo, { ">\t$jumpTo" })
    class Condition(val conditionJumpTo: Int, jumpTo: Int) : Command(jumpTo, { "?\t$jumpTo,$conditionJumpTo" }, false)
    class Finale : Command(-1, { ".\t" }, false)
    class Halt : Command(-1, { "!\t" }, false)

    private val map: Map<Int, Command>

    init {
        val str = fromFile(path)
        val map = mutableMapOf<Int, Command>()
        str.split('\n').forEach {
            if (!it.matches(Regex("\\s*//.*"))) {
                val cmd = it.split('.')
                val index = cmd[0].trim().toInt()
                val cmd2 = cmd[1].split(',')
                val jumpTo = cmd2.getOrElse(1) { "-1" }.trim().toInt()
                val command = when (cmd2[0].trim()) {
                    "V" -> Mark(jumpTo)
                    "X" -> Unmark(jumpTo)
                    "<-" -> Left(jumpTo)
                    "->" -> Right(jumpTo)
                    "!" -> Finale()
                    else -> Condition(cmd2[0].trim().toInt(), jumpTo)
                }
                map[index] = command
            }
        }
        this.map = map.toMap()
    }

    operator fun get(int: Int) = map.getOrDefault(int, Halt())

    fun forEach(action: (Pair<Int, Command>) -> Unit) {
        map.keys.sorted().forEach {
            action(Pair(it, this[it]))
        }
    }
}