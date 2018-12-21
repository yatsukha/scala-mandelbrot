ThisBuild / scalaVersion := "2.12.7"

name := "mandelbrot"
version := "1.0"

//allow ctrl-c to interrupt running program rather than JVM as a whole
cancelable in Global := true