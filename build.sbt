ThisBuild / scalaVersion := "2.12.7"

name := "mandelbrot"
version := "1.0"

libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.1"

cancelable in Global := true