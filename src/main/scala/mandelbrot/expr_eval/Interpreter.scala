package mandelbrot.expr_eval

class Interpreter extends Evaluator {
    println(
        "Interpreter/evaluator for debugging Symbolic Parser.\nFor complex numbers use symbols with %f %f notation for real and imaginary part."
    )

    private val action = Set(
        "eval",
        "reset", "clear",
        "quit"
    )

    println("Available commands:")
    action foreach println _

    private val separator = ";"
    private val trim = true

    import mandelbrot.complex._

    private val symbols = collection.mutable.Map[String, Complex]()

    type Expression = Expr
    type Result = Complex

    private var exit = false
    private var expr: Expr = Empty

    while (!exit) {
        finePrint("$ ")

        val commands = """\s+""".r.replaceAllIn(io.StdIn.readLine, "").split(";")

        for (cmd <- commands) cmd match {
            case "eval" => println(if (expr != Empty) eval(expr) else "/")
            case "reset" => { symbols.clear; expr = Empty }
            case "clear" => { symbols.clear; }
            case "quit" => exit = true
            case _ => expr = {
                val parser = ComplexSymbolParser()
                val ret = parser.parse(parser.expr_a, cmd).get

                println(s"Intepreted as: $ret")

                ret
            }
        }
    }

    def eval(expr: Expression): Result = expr match {
        case Empty => throw new IllegalStateException
        case Com(c) => c
        case Op(l, r, Lambda(_, op)) => op(eval(l), eval(r))
        case Sym(s) =>
            if (symbols contains s) {
                symbols(s)
            } else {
                finePrint(s + " = ")
                val line = io.StdIn.readLine.split(" ")

                val sc = Complex(line(0).toDouble, line(1).toDouble)
                symbols += s -> sc

                sc
            }
    }

    def finePrint(msg: String): Unit = {
        print(msg)
        Console.out.flush
    }
}

object StartInterpreter {
    def apply(): Unit = 
        new Interpreter()
}