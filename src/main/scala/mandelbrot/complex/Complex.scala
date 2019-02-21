package mandelbrot.complex

import scala.math.sqrt

/**
 * Basic complex number representation.
 */ 
class Complex private (var r: Double, var i: Double) {    
    def abs = sqrt(r * r + i * i)

    def +(that: Complex) = Complex(this.r + that.r, this.i + that.i)
    def -(that: Complex) = this.+(Complex(-that.r, -that.i))
    def *(that: Complex) = Complex(this.r * that.r - this.i * that.i, this.r * that.i + that.r * this.i) 

    override
    def toString = s"$r${if (i.toInt >= 0) "+" else ""}${i}i"
}

object Complex {
    def apply(r: Double, i: Double): Complex = 
        new Complex(r, i)
}