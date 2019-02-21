package mandelbrot.expr_eval

import mandelbrot.complex.Complex

/**
 * Evalutes expression in form of a cached parse tree.
 */
class SymbolicEvaluator private (expr: String, val syms: collection.mutable.Map[String, Complex]) extends Evaluator {
    type Expression = Expr
    type Result = Complex

    val parseTree = {
        val parser = ComplexSymbolParser()

        parser.parse(parser.expr_a, expr).get
    }

    def eval(expr: Expression): Result = expr match {
        case Sym(s) => syms(s)
        case Com(c) => c
        case Op(l, r, Lambda(_, op)) => op(eval(l), eval(r))
        case Empty => Complex(0, 0)
    }

    override
    def toString: String =
        parseTree.toString

}

object SymbolicEvaluator {
    def apply(expr: String, syms: collection.mutable.Map[String, Complex]): SymbolicEvaluator =
        new SymbolicEvaluator(expr, syms)
}