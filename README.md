# Mandelbrot set variation #

Simple scala plotter that draws Mandelbrot variant <a href="https://www.codecogs.com/eqnedit.php?latex=z&space;=&space;z^2&space;&plus;&space;0.19&space;z^3&space;&plus;&space;x" target="_blank"><img src="https://latex.codecogs.com/gif.latex?z&space;=&space;z^2&space;&plus;&space;0.19&space;z^3&space;&plus;&space;c" title="z = z^2 + 0.19 z^3 + c" /></a>

Implementation consists of classes for complex numbers, image manipulation and the plotting itself. Basic usage is demonstrated in Main.scala but most of the code should be self explanatory anyways. Image manipulation is done trough java.awt.

I also added support for custom variations as I needed practice with scala parsers. Keep in mind that drawing with custom input will be 2 orders of magnitude slower because the parse tree needs to be evaluated for every iteration.

Some examples below.

## 1000 iterations: ##

![1000](/images/small_1000.png)

## 200 iterations: ##

![200](/images/small_200.png)