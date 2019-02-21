package mandelbrot.expr_eval

class Interpreter extends Evaluator {
    println(
"""Interpreter for evaluating expressions and drawing mandelbrots :P

Commands (can be chained with ;):
<expression> - expression with symbols or real numbers, complex can be provided as symbols,
               everything that is not a command from below defaults to this
eval         - evaluate latest expression, if symbols are missing from symbol table you can enter their
               value in form %f %f, real and imaginary part respectively, when prompted
reset        - reset symbol table and expression
clear        - reset only symbol table
quit         - exit the interpreter into the caller
draw         - output the corresponding mandelbrot variation to a file,
               you will be prompted for width, height and other information
               note that NO expression checking is done, it is up to you to provide a valid expression
               that the plotter can use (only symbols allowed are z and c together with other real numbers)"""
    )

    private val action = Set(
        "eval",
        "reset", 
        "clear",
        "quit",
        "draw"
    )

    private val separator = ";"

    import mandelbrot.complex._

    private val symbols = collection.mutable.Map[String, Complex]()

    type Expression = Expr
    type Result = Complex

    private var exit = false
    private var expr: Expr = Empty
    private var exprStr: String = ""

    while (!exit) {
        finePrint("$ ")

        val commands = """\s+""".r.replaceAllIn(io.StdIn.readLine, "").split(separator)

        for (cmd <- commands) cmd match {
            case "eval" => println(if (expr != Empty) eval(expr) else "/")
            case "reset" => { symbols.clear; expr = Empty }
            case "clear" => symbols.clear
            case "quit" => exit = true
            case "draw" => {
                if (expr != Empty) {
                    println("Enter everything asked in a single line!")

                    finePrint("Enter width and height: ")
                    var tokens = io.StdIn.readLine.trim.split(" ")
                    val (width, height) = (tokens(0).toInt, tokens(1).toInt)
                    
                    finePrint("Enter number of iterations and escape distanc: ")
                    tokens = io.StdIn.readLine.trim.split(" ")
                    val (n, dist) = (tokens(0).toInt, tokens(1).toDouble)

                    finePrint("Enter the output file with filetype: ")
                    val output = io.StdIn.readLine.trim

                    import mandelbrot.Plotter

                    val p = Plotter(
                        width,
                        height,
                        n,
                        dist,
                        exprStr
                    )

                    println("Drawing...")
                    p.draw
                    p.write(output)
                    println("Done.")
                } else {
                    println("No expression was entered!")
                }
            }
            case _ => expr = {
                val parser = ComplexSymbolParser()
                val ret = parser.parse(parser.expr_a, cmd).get
                    exprStr = cmd

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