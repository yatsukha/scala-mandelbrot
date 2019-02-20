package mandelbrot

//example program
object Main extends App {
    val p = Plotter(
        width = 1920,
        height = 1080,
        maxDistance = 500,
        maxIterations = 100,
        expr = "z^2+2*c"
    )

    p.draw
    p.write("images/symbolic.png")
}