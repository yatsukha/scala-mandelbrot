package mandelbrot.expr_eval

trait Evaluator {
    type Expression
    type Result

    def eval(expr: Expression): Result
}