import sbt._

object Dependencies {
  val scalaV = "2.11.8"

   val playJson      = "com.typesafe.play" %% "play-json" % "2.4.3"
   val betterFiles   = "com.github.pathikrit" %% "better-files" % "2.16.0"
   val codacyEngine  = "com.codacy" %% "codacy-engine-scala-seed" % "2.6.31"
  val scalaReflect  = "org.scala-lang" % "scala-reflect"  % scalaV

  //val patterns      = "com.codacy" %% "codacy-patterns-core"  % "1.0-SNAPSHOT" withSources()

}
