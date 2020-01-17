import sbt._

object Dependencies {
  val scalaV = "2.12.8"

  val betterFiles = "com.github.pathikrit" %% "better-files" % "3.6.0"
  val codacyEngine = "com.codacy" %% "codacy-engine-scala-seed" % "3.1.0"
  val scalaReflect = "org.scala-lang" % "scala-reflect" % scalaV

  //val patterns      = "com.codacy" %% "codacy-patterns-core"  % "1.0-SNAPSHOT" withSources()

}
