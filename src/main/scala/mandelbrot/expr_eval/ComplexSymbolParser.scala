package mandelbrot.expr_eval

import scala.util.parsing.combinator._
import mandelbrot.complex.Complex

/**
 * Case classes for building a parse tree for later evaluation, and the parser itself.
 * Note that parser was kept as simple as possible and therefore doesnt have some features
 * you would want for broader usage.
 */

sealed abstract class Expr

case class Sym(value: String) extends Expr 
case class Com(value: Complex) extends Expr
case class Op(left: Expr, right: Expr, op: (Complex, Complex) => Complex) extends Expr

class ComplexSymbolParser private extends RegexParsers {
    val double = """(\d+(\.\d+)?)""".r
    val symbol = "[a-zA-Z]+".r

    def factor: Parser[Expr] =
        double ^^ { d => Com(Complex(d.toDouble, 0)) } | symbol ^^ { s => Sym(s) } | "(" ~> expr_b <~ ")"

    def term: Parser[Expr] =
        (factor ~ opt(("^" | "*") ~ term)) ^^ {
            case f ~ None => f
            case f ~ Some("*" ~ g) => Op(f, g, _ * _)
            case f ~ Some("^" ~ g) => Op(f, g, (x: Complex, y: Complex) => {
                var ret = Complex(1, 0)

                (1 to (y.r + 0.0001).toInt).foreach(_ => ret = ret * x)

                ret
            })
            case _ => throw new IllegalStateException
        }

    def expr_a: Parser[Expr] =
        (expr_b ~ opt(("+" | "-") ~ expr_b)) ^^ {
            case m ~ None => m
            case m ~ Some("+" ~ n) => Op(m, n, _ + _)
            case m ~ Some("-" ~ n) => Op(m, n, _ - _)
            case _ => throw new IllegalStateException
        }

    def expr_b: Parser[Expr] = 
        (term ~ opt(("+" | "-") ~ term)) ^^ {
            case m ~ None => m
            case m ~ Some("+" ~ n) => Op(m, n, _ + _)
            case m ~ Some("-" ~ n) => Op(m, n, _ - _)
            case _ => throw new IllegalStateException
        }
}

object ComplexSymbolParser {
    def apply(): ComplexSymbolParser =
        new ComplexSymbolParser()
}