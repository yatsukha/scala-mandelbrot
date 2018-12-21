package mandelbrot.imgmanip

//something to draw on
import java.awt.image.BufferedImage

//draw drawing pixels in color
import java.awt.{Graphics2D, Color}

//basic image class
//handles creation, drawing and writing to a file
class Image private (val width: Int, val height: Int) {
    //check if size is valid
    if (width <= 0 || height <= 0)
        throw new IllegalArgumentException("Invalid image size.")

    //canvas to draw on
    private val canvas = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

    //draw interface
    private val graphics = canvas.createGraphics

    //true image was written and disposed, false otherwise
    var _disposed = false
    def disposed  = _disposed

    //set pixel at (x, y) to color from hue
    //using hue instead of standard rgb because it better suits coloring algorithm
    def setPixel(x: Int, y: Int, hue: Float) {
        //if image was disposed there is nothing to draw on
        if (_disposed)
            throw new IllegalStateException("Image was disposed.")
        //basic bounds check
        else if (x < 0 || x >= width || y < 0 || y >= height)
            throw new IllegalArgumentException("Pixel out of bounds.")

        //inline rgb from hue; saturation and brighness hardcoded :P
        val rgb = Color.HSBtoRGB(hue, 0.6f, 1.0f)

        //set color of the pen, rgb components are isolated
        graphics.setColor(new Color((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF))

        //fill a 1 by 1 rectangle, essentially a pixel
        graphics.fillRect(x, y, 1, 1)
    }

    //write image to file and dispose of it
    def write(file: String) {
        //writing
        javax.imageio.ImageIO.write(canvas, "png", new java.io.File(file))

        //disposal
        graphics.dispose()
        _disposed = true
    }
}

//companion object
object Image {
    def apply(width: Int, height: Int) = new Image(width, height)
}