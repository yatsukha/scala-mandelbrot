package mandelbrot.complex

//abs
import math.sqrt

//class representing complex numbers
//didnt use another lib since it is easy to implement
class Complex private (var r: Double, var i: Double) {
    //absolute value
    def abs = sqrt(r * r + i * i)

    //binary +
    def +(that: Complex) = Complex(this.r + that.r, this.i + that.i)

    //binary *
    def *(that: Complex) = Complex(this.r * that.r - this.i * that.i, this.r * that.i + that.r * this.i)

    //binary * with a const
    def *(that: Double) = Complex(this.r * that, this.i * that)

    //debugging
    override
    def toString = s"$r${if (i >= 0) "+" else ""}$i"
}

//companion object
object Complex {
    def apply(r: Double, i: Double) = new Complex(r, i)
}