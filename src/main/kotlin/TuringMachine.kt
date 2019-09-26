import java.io.File
import java.io.PrintWriter

class TuringMachine(
    val line: Tape = Tape(fromFile("turing/tape.txt")),
    val table: TuringMachineTable = TuringMachineTable("turing")
) {
    private var state = 0

    init {
        val pw = PrintWriter(File("turing/output.txt"))
        do {
            val curr = table[line.current, state]
            pw.println("Q$state:  $curr")
            pw.println(line)
            if (curr.action != TuringMachineTable.Action.HALT) {
                line.current = curr.character
                state = curr.state
                line.shift(curr.direction.shiftRight)
            }
        } while (curr.action == TuringMachineTable.Action.NONE)
        pw.close()
    }
}

