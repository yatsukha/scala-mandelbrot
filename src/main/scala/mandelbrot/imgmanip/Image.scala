package mandelbrot.imgmanip

import java.awt.image.BufferedImage
import java.awt.Color

class Image private (val width: Int, val height: Int) {
    if (width <= 0 || height <= 0)
        throw new IllegalArgumentException("Invalid image size.")

    private val canvas = 
        new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

    private val graphics = canvas.createGraphics

    var _disposed = false
    def disposed  = _disposed

    var saturation = 0.6f

    def setPixel(x: Int, y: Int, hue: Float, brightness: Float) {
        if (_disposed)
            throw new IllegalStateException("Image was disposed.")
        else if (x < 0 || x >= width || y < 0 || y >= height)
            throw new IllegalArgumentException("Pixel out of bounds.")
        
        val rgb = Color.HSBtoRGB(hue, saturation, brightness)

        graphics.setColor(new Color((rgb >> 16) & 0xFF, 
                                    (rgb >> 8) & 0xFF, 
                                     rgb & 0xFF))
                                     
        graphics.fillRect(x, y, 1, 1)
    }

    def write(file: String) {
        javax.imageio.ImageIO.write(canvas, "png", new java.io.File(file))

        graphics.dispose()
        _disposed = true
    }
}

object Image {
    def apply(width: Int, height: Int) = new Image(width, height)
}