package mandelbrot

//example program
object Main extends App {
    val p = Plotter(2000, 1000)
        p.draw
        p.write("images/mandelbrot_set.png")
}