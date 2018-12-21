package mandelbrot

//image to draw on
import imgmanip.Image

//complex numbers
import complex.Complex

//plots mandelbrot set
//NOTE: recommended scaling is 2:1, otherwise you need to change xrange and yrange because the image will be skewed
class Plotter private (val width: Int, val height: Int, val iterations: Int) {
    //validate number of iterations
    if (iterations <= 0)
        throw new IllegalArgumentException("Invalid number of iterations.")

    //draws onto this
    val img = Image(width, height)

    //formula: z -> z^2 + lambda * z^3 + c
    private val lambda: Double = 0.19

    //determine whether the point will escape or not
    private val maxDistance = 200.0

    //range that is plotted
    private val xrange = (-5.5, 1.2)

    //yrange is calculated in relation to xrange based on assumption that the image aspect ratio will be 2:1
    private val yrange = (-0.25 * math.abs(xrange._1 - xrange._2), 0.25 * math.abs(xrange._1 - xrange._2))

    //scale from img indices to complex plane
    def scaleToComplex(x: Int, y: Int) = Complex(xrange._1 + x.toDouble / width.toDouble  * (xrange._2 - xrange._1),
                                                 yrange._1 + y.toDouble / height.toDouble * (yrange._2 - yrange._1)) 
    
    //draw on img
    def draw {
        //for each pixel on image
        for (x <- 0 until width; y <- 0 until height) {
            //constant
            val c = scaleToComplex(x, y)

            //starting point
            var z = Complex(0, 0)

            //current iteration
            var iteration = 0

            //while point is within max distance iterate
            while (z.abs <= maxDistance && iteration < iterations) {
                z = z * z + z * z * z * lambda + c
                iteration += 1
            }
            
            //scaling for color uses number of iterations and the last distance of point
            val scale = 1.0 - (iteration.toDouble - math.log(math.log(z.abs)) / math.log(2)) / iterations.toDouble

            //set x,y to hue
            img.setPixel(x, y, (0.7 + 10 * scale).toFloat)
        }
    }

    //write to file 
    def write(file: String) { img.write(file) }
}

//companion object
object Plotter {
    def apply(width: Int, height: Int, iterations: Int = 1000) = new Plotter(width, height, iterations)
}