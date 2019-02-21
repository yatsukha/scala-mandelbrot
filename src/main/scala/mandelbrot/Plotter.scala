package mandelbrot

import mandelbrot.imgmanip.Image
import mandelbrot.complex.Complex

/**
 * Main plotter class.
 * Note that some options are hardcoded such as range.
 * Expected image aspect ratio is 2:1.
 */
class Plotter private (val width: Int, val height: Int, val maxIterations: Int, val maxDistance: Double, val expr: String) {
    if (maxIterations <= 0)
        throw new IllegalArgumentException("Invalid number of iterations.")

    private val img = Image(width, height)

    private val xrange = (-5.5, 1.2)
    private val yrange = 
        (-0.25 * math.abs(xrange._1 - xrange._2), 
          0.25 * math.abs(xrange._1 - xrange._2))
    
    def scaleToComplex(x: Int, y: Int) = 
        Complex(xrange._1 + x.toDouble / width.toDouble  * (xrange._2 - xrange._1),
                yrange._1 + y.toDouble / height.toDouble * (yrange._2 - yrange._1))

    var baseColor = 0.5
    var colorMultiplier = 0.5

    var hueGenerator = (point: Complex, _: Int, _: Int) => 1 - 1 / (1 + math.log(point.abs))
    var brightnessGenerator = (point: Complex, _: Int, _: Int) => 1.0

    private var escapeIterator: (Complex, Int) => (Complex, Int) = (c, n) => {
        var z = Complex(0, 0)
        var iterations = 0
        val lambda = Complex(0.19, 0)

        while (z.abs <= maxDistance && iterations < n) {
            z = z * z + z * z * z * lambda + c

            iterations += 1
        }

        (z, iterations)
    }

    if (expr.length > 0) {
        import mandelbrot.expr_eval._

        val evaluator = SymbolicEvaluator(expr, collection.mutable.Map(
            "z" -> Complex(0, 0),
            "c" -> Complex(0, 0)
        ))

        println(s"Custom function: $evaluator")

        escapeIterator = (c, n) => {
            var iterations = 0

            evaluator.syms("c") = c
            evaluator.syms("z") = Complex(0, 0)

            while (evaluator.syms("z").abs <= maxDistance && iterations < n) {
                evaluator.syms("z") = evaluator.eval(evaluator.parseTree)

                iterations += 1
            }

            (evaluator.syms("z"), iterations)
        }
    }
    
    def draw: Unit = {
        for (x <- 0 until width; y <- 0 until height) {
            var (z, iterations) = escapeIterator(scaleToComplex(x, y), maxIterations)
            
            img.setPixel(x, y, 
                (baseColor + colorMultiplier * hueGenerator(z, iterations, maxIterations)).toFloat, 
                 brightnessGenerator(z, iterations, maxIterations).toFloat)
        }
    }

    def write(file: String): Unit =
        img.write(file)
}

object Plotter {
    def apply(width: Int, height: Int, maxIterations: Int, maxDistance: Double, expr: String = ""):Plotter = 
        new Plotter(width, height, maxIterations, maxDistance, expr)
}