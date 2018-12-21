# Mandelbrot set variation #

Simple scala plotter that draws Mandelbrot variant <a href="https://www.codecogs.com/eqnedit.php?latex=z&space;=&space;z^2&space;&plus;&space;0.19&space;z^3&space;&plus;&space;x" target="_blank"><img src="https://latex.codecogs.com/gif.latex?z&space;=&space;z^2&space;&plus;&space;0.19&space;z^3&space;&plus;&space;x" title="z = z^2 + 0.19 z^3 + x" /></a>

Implementation consists of classes for complex numbers, image manipulation and the plotting itself. Basic usage is demonstrated in Main.scala and **below**. Image manipulation is done trough java.awt.

Some examples below.

**Note**: pictures are really big so they might not load for you.

## 1000 iterations: ##

![1000](/images/1000.png)

## 200 iterations: ##

![200](/images/200.png)

## Usage ##

### Image class ###

The class itself has a private constructor and can only be instantiated trough companion object.
It takes image width and height as parameters and constructs a canvas. 

Method setPixel is used to draw pixels. It takes the coordinates of pixel where (0, 0) is upper left corner of the image, and hue from HSB color representation. Note that S - saturation and B - brightness are hardcoded, as that functionality wasn't needed for this project.

Method write is used to write the image to a real file. It takes file location as a parameter. After the image has been written to a file the graphics handle is disposed and therefor you can not draw using this instance of class anymore.

### Complex class ##

This is just a simple implementation of complex numbers, not much to say here.

Object of this class is created trough companion object with real and imaginary parts as parameters. With abs you can fetch the absolute value of the complex number. Note that + is overloaded once and * is overloaded twice to support adding multiple complex numbers and multiplying them.

### Plotter class ###

**Note**: plotter asumes the image aspect ratio is 2:1.

This is the class that gets the work done. It initializes an Image object with width and height given by parameters. Then with draw method it iterates over every pixel mapping it to a complex number with scaleToComplex method and determines how fast the point will escape outside of maxDistance. After that you can write to a file using write which is just a call to Image.write.

Few things to notice here: range is hardcoded as a private value so its's not so hard to change. The Mandelbrot formula is contained within draw method inside of loop as well as the algorithm used to find the hue based on escape speed.