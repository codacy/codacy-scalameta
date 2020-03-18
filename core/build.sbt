import Dependencies._

name := "codacy-patterns-core"

//core dependencies
val scalaLib = "org.scala-lang" % "scala-library" % scalaV
val scalaCompiler = "org.scala-lang" % "scala-compiler" % scalaV
val scalameta = "org.scalameta" %% "scalameta" % "4.0.0" withSources ()
val scalametaContrib = "org.scalameta" %% "contrib" % "4.0.0" withSources ()

scalaVersion := scalaV

organization := "com.codacy"

libraryDependencies ++= Seq(scalameta, scalametaContrib, scalaLib, scalaReflect, scalaCompiler)

startYear := Some(2016)

description := "Codacy Patterns Core"

licenses := Seq("The Apache Software License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

homepage := Some(url("https://github.com/codacy/codacy-scalameta"))

// HACK: This setting is not picked up properly from the plugin
pgpPassphrase := Option(System.getenv("SONATYPE_GPG_PASSPHRASE")).map(_.toCharArray)

scmInfo := Some(
  ScmInfo(url("https://github.com/codacy/codacy-scalameta"),
          "scm:git:git@github.com:codacy/codacy-scalameta.git")
)

publicMvnPublish
