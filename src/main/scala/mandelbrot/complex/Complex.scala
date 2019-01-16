package mandelbrot.complex

import scala.math.sqrt

class Complex private (var r: Double, var i: Double) {    
    def abs = sqrt(r * r + i * i)

    def +(that: Complex) = Complex(this.r + that.r, this.i + that.i)
    def *(that: Complex) = Complex(this.r * that.r - this.i * that.i, this.r * that.i + that.r * this.i)    
    def *(that: Double)  = Complex(this.r * that, this.i * that)

    override
    def toString = s"$r${if (i >= 0) "+" else ""}$i"
}

object Complex {
    def apply(r: Double, i: Double) = new Complex(r, i)
}