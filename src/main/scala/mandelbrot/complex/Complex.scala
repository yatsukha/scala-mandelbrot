package mandelbrot.complex

import math._

/**
 * Basic complex number representation.
 */ 
class Complex private (var r: Double, var i: Double) {    
    def abs = hypot(r, i)

    def +(that: Complex) = Complex(this.r + that.r, this.i + that.i)
    def -(that: Complex) = this.+(Complex(-that.r, -that.i))
    def *(that: Complex) = Complex(this.r * that.r - this.i * that.i, this.r * that.i + that.r * this.i) 

    def /(that: Complex) = {
        val u = this * (that~)
        val v = that * (that~)

        Complex(u.r / v.r, u.i / v.r)
    }

    def ^(that: Complex) = {
        val arg = atan2(this.i, this.r)
        val sqr = this.abs * this.abs
        val pre = pow(sqr, that.r / 2.0) * exp(-1.0 * that.i * arg)
        val suf = that.r * arg + 0.5 * that.i * log(sqr)

        if (sqr < 0.000001)
            Complex(0, 0)
        else
            Complex(
                pre * cos(suf),
                pre * sin(suf)
            )
    }

    def ~(): Complex = 
        Complex(this.r, -this.i)

    override
    def toString: String = 
        f"${
            if (r < 0) "- "
            else ""
        }${math.abs(r)}%.2f ${
            if (i >= 0.0) "+ "
            else "- "
        }${math.abs(i)}%.2fi"
}

object Complex {
    def apply(r: Double, i: Double): Complex = 
        new Complex(r, i)
}