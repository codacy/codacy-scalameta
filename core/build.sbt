name := """codacy-patterns-core"""

val scalaV = "2.11.8"
//core dependencies
val scalaLib      = "org.scala-lang" % "scala-library"  % scalaV
val scalaReflect  = "org.scala-lang" % "scala-reflect"  % scalaV
val scalaCompiler = "org.scala-lang" % "scala-compiler" % scalaV
val scalameta     = "org.scalameta" %% "scalameta" % "1.6.0" withSources()
val commonsIO     = "commons-io" % "commons-io" % "2.4"
val scalaXml      = "org.scala-lang.modules" %% "scala-xml" % "1.0.4"

version := "0.1.2"

scalaVersion := scalaV

organization := "com.codacy"

libraryDependencies ++= Seq(
  scalameta,
  scalaLib,
  scalaReflect,
  scalaCompiler
)

organizationName := "Codacy"

organizationHomepage := Some(new URL("https://www.codacy.com"))

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (version.value.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

startYear := Some(2016)

description := "Codacy Patterns Core"

licenses := Seq("The Apache Software License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

homepage := Some(url("https://github.com/codacy/codacy-scalameta"))

pomExtra :=
  <scm>
    <url>https://github.com/codacy/codacy-scalameta</url>
    <connection>scm:git:git@github.com:codacy/codacy-scalameta.git</connection>
    <developerConnection>scm:git:git@github.com:codacy/codacy-scalameta.git</developerConnection>
  </scm>
    <developers>
      <developer>
        <id>johannegger</id>
        <name>Johann</name>
        <email>johann [at] codacy.com</email>
        <url>https://github.com/johannegger</url>
      </developer>
    </developers>
