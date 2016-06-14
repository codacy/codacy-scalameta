name := """codacy-patterns-core"""

val scalaV = "2.11.8"
//core dependencies
val scalaLib      = "org.scala-lang" % "scala-library"  % scalaV
val scalaReflect  = "org.scala-lang" % "scala-reflect"  % scalaV
val scalaCompiler = "org.scala-lang" % "scala-compiler" % scalaV
val scalameta     = "org.scalameta" %% "scalameta" % "0.23.0" withSources()
val commonsIO     = "commons-io" % "commons-io" % "2.4"
val scalaXml      = "org.scala-lang.modules" %% "scala-xml" % "1.0.4"

version := "1.0-SNAPSHOT"

scalaVersion := scalaV

organization := "com.codacy"

libraryDependencies ++= Seq(
  scalameta,
  scalaLib,
  scalaReflect,
  scalaCompiler
)

//publish pattern codes as resources
