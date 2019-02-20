package mandelbrot.expr_eval

import mandelbrot.complex.Complex

/**
 * Evalutes expression in form of a cached parse tree.
 */
class SymbolicEvaluator private (expr: String, val syms: collection.mutable.Map[String, Complex]) {
    type Expression = Expr
    type Result = Complex

    val parseTree = {
        val parser = ComplexSymbolParser()

        parser.parse(parser.expr_a, expr).get
    }

    def eval(expr: Expression): Result = expr match {
        case Sym(s) => syms(s)
        case Com(c) => c
        case Op(l, r, op) => op(eval(l), eval(r))
    }

    private def walker(current: Expr): String = s"(${current match {
        case Sym(s) => s
        case Com(c) => c.toString
        case Op(l, r, _) => s"op ${walker(l)} ${walker(r)}"
    }})"

    override
    def toString: String =
        walker(parseTree)

}

object SymbolicEvaluator {
    def apply(expr: String, syms: collection.mutable.Map[String, Complex]): SymbolicEvaluator =
        new SymbolicEvaluator(expr, syms)
}