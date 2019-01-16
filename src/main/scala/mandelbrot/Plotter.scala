package mandelbrot

import mandelbrot.imgmanip.Image
import mandelbrot.complex.Complex

//NOTE: recommended scaling is 2:1, otherwise you need to change xrange and yrange because the image will be skewed
class Plotter private (val width: Int, val height: Int, val maxIterations: Int) {
    if (maxIterations <= 0)
        throw new IllegalArgumentException("Invalid number of iterations.")

    private val img = Image(width, height)

    //formula: z -> z^2 + lambda * z^3 + c
    private val lambda: Double = 0.19

    private val maxDistance = 200.0

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
    
    def draw {
        for (x <- 0 until width; y <- 0 until height) {
            val c = scaleToComplex(x, y)
            var z = Complex(0, 0)
            var iterations = 0

            while (z.abs <= maxDistance && iterations < maxIterations) {
                z = z * z + z * z * z * lambda + c

                iterations += 1
            }
            
            img.setPixel(x, y, 
                (baseColor + colorMultiplier * hueGenerator(z, iterations, maxIterations)).toFloat, 
                 brightnessGenerator(z, iterations, maxIterations).toFloat)
        }
    }

    def write(file: String) { img.write(file) }
}

object Plotter {
    def apply(width: Int, height: Int, maxIterations: Int = 1000) = new Plotter(width, height, maxIterations)
}