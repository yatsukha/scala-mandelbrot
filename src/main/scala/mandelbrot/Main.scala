package mandelbrot

//example program
object Main extends App {
    val p = Plotter(1600, 800, 1000)

    p.draw
    p.write("images/out.png")
}