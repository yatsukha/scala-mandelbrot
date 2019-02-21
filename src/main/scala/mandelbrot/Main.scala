package mandelbrot

/**
 * Example program.
 */
object Main extends App {
    //mandelbrot.expr_eval.StartInterpreter()

    val plotter = Plotter(
        width = 2000,
        height = 1000,
        maxIterations = 1000,
        maxDistance = 500,
        expr = "z^2+0.19*z^3+c" //remove this bit for speed gain
    )

    plotter.draw
    plotter.write("images/symbolic.png")
}