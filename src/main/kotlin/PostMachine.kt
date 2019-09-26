import PostMachineProgram.*
import java.io.File
import java.io.PrintWriter
import java.nio.charset.Charset


class PostMachine(
    val tape: Tape = Tape(fromFile("post/tape.txt")),
    val program: PostMachineProgram = PostMachineProgram("post/program.txt")
) {
    private val initialTape: String

    init {
        val pw = PrintWriter(File("post/output.txt"))
        var pc = 0
        var cc = 1
        initialTape = tape.toString()
        machine@ while (true) {
            val command = program[cc]
            pw.println("$cc: ${command.javaClass.simpleName}")
            pw.println(tape.toFancyString())
            when (command) {
                is Mark -> if (tape.current != '1') tape.current = '1'
                is Unmark -> if (tape.current == '1') tape.current = ' '
                is Left -> tape.left()
                is Right -> tape.right()
                is Condition ->
                    if (tape.current == '1')
                        cc = command.conditionJumpTo
                    else
                        cc = command.jumpTo
                is Halt -> {
                    System.err.println("HALT!")
                    break@machine
                }
                is Finale -> break@machine
            }
            if (command.jump)
                cc = command.jumpTo
            if (pc == cc)
                break@machine
            pc = cc
            //Thread.sleep(10)
        }
        pw.close()
    }

    fun toAlgo2000(file: File) {
        val header = "\tКоманда\tПереход\tЖивотноводство"
        val pw = PrintWriter(file, Charset.forName("Windows-1251"))
        val br = "\r\n"
        var cc = 0
        fun append(str: String) {
            cc += str.length
        }
        append("$header$br")
        program.forEach {
            append("${it.first}\t${it.second.string()}\t$br")
        }
        pw.append("\u0000\u0000\u0000\u0000")
        pw.append(cc.toByte().toChar())
        pw.append("\u0002\u0000\u0000$header$br")
        program.forEach {
            pw.append("${it.first}\t${it.second.string()}\t$br")
        }
        pw.append("\u0000\u0000\u0000\u0000")
        pw.append((initialTape.length - 1).toByte().toChar())
        pw.append("\u0000\u0000\u0000\u0000")
        pw.append("\u0000\u0000\u0000\u0002\u0000\u0000\u0000\u0001\u0000\u0000\u0000")
        pw.append("$initialTape")
        pw.close()
    }
}