package mandelbrot.expr_eval

import scala.util.parsing.combinator._
import mandelbrot.complex.Complex

/**
 * Case classes for building a parse tree for later evaluation, and the parser itself.
 * Note that parser was kept as simple as possible and therefore doesnt have some features
 * you would want for broader usage.
 */

/**
 * This exists only to provide nice toString in pair with a function.
 */
sealed abstract class Func

case class Lambda(op: String, value: (Complex, Complex) => Complex) extends Func {
    override
    def toString: String =
        op
}

/**
 * Parse tree components with readable toString
 */
sealed abstract class Expr

case class Sym(value: String) extends Expr { 
    override
    def toString: String =
        value
}

case class Com(value: Complex) extends Expr {
    override
    def toString: String =
        value.toString
}

case class Op(left: Expr, right: Expr, fn: Func) extends Expr {
    override
    def toString: String =
        s"($left) $fn ($right)"
}

object Empty extends Expr

class ComplexSymbolParser private extends RegexParsers {
    val double = """(\d+(\.\d+)?)""".r
    val imaginary = """(\d+(\.\d+)?)i""".r
    val symbol = "[a-zA-Z]+".r

    def factor: Parser[Expr] =
        imaginary ^^ { i => Com(Complex(0, i.dropRight(1).toDouble)) } |
        double ^^ { d => Com(Complex(d.toDouble, 0)) } | 
        symbol ^^ { s => Sym(s) } | 
        "(" ~> expr_b <~ ")"

    def term: Parser[Expr] =
        (factor ~ opt(("^" | "*" | "/") ~ term)) ^^ {
            case f ~ None => f
            case f ~ Some("*" ~ g) => Op(f, g, Lambda("*", _ * _))
            case f ~ Some("/" ~ g) => Op(f, g, Lambda("/", _ / _))
            case f ~ Some("^" ~ g) => Op(f, g, Lambda("^", _ ^ _))
            case _ => throw new IllegalStateException
        }

    def expr_a: Parser[Expr] =
        (expr_b ~ opt(("+" | "-") ~ expr_b)) ^^ {
            case m ~ None => m
            case m ~ Some("+" ~ n) => Op(m, n, Lambda("+", _ + _))
            case m ~ Some("-" ~ n) => Op(m, n, Lambda("-", _ - _))
            case _ => throw new IllegalStateException
        }

    def expr_b: Parser[Expr] = 
        (term ~ opt(("+" | "-") ~ term)) ^^ {
            case m ~ None => m
            case m ~ Some("+" ~ n) => Op(m, n, Lambda("+", _ + _))
            case m ~ Some("-" ~ n) => Op(m, n, Lambda("-" ,_ - _))
            case _ => throw new IllegalStateException
        }
}

object ComplexSymbolParser {
    def apply(): ComplexSymbolParser =
        new ComplexSymbolParser()
}