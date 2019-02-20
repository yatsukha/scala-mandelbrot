package mandelbrot

//example program
object Main extends App {
    val p = Plotter(
        width = 1920,
        height = 1080,
        maxDistance = 500,
        maxIterations = 1000,
        expr = "z^2+0.19*z^3+c" //this is the default, remove for speed
    )

    p.draw
    p.write("images/symbolic.png")
}