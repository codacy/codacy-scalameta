import com.typesafe.sbt.packager.docker._
import sbt.Keys._
import sbt._
import Dependencies._

resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"

name := """codacy-engine-scalameta"""

version := "1.0-SNAPSHOT"

scalaVersion := scalaV

organization := "com.codacy"

lazy val root = project.in(file("."))
  .dependsOn(common,macros,genResources,basePatterns)

//generates the resources a codacy-engine expects
lazy val genResources = project.in(file("genResources"))
  .settings(scalaVersion := scalaV)
  .dependsOn(macros,common)

//the core that defines patterns and results
lazy val core = project.in(file("core"))
  .settings(scalaVersion := scalaV)

//our base package of scala patterns
lazy val basePatterns = project.in(file("patterns-base"))
  .settings(scalaVersion := scalaV)
  .dependsOn(core)
  .settings(
    mappings in (Compile, packageBin) <++= (scalaSource in Compile) map { sourceDir =>

      val files = sourceDir / "codacy" / "patterns"
      val dest = "patterns"

      for {
        path <- files.listFiles().toList if path.toString.endsWith(".scala")
      } yield path -> s"${dest}/${path.name}"
    }
  )

//the bridge between a codacy-engine and scala.meta patterns
lazy val common = project.in(file("common"))
  .dependsOn(core)
  .settings(scalaVersion := scalaV)
  .settings( libraryDependencies ++= Seq(
    Dependencies.codacyEngine,
    Dependencies.playJson
  ))

lazy val macros = project.in(file("macros")).
  settings(scalaVersion := scalaV).
  settings(libraryDependencies += Def.setting(scalaReflect).value).
  settings(libraryDependencies += betterFiles).
  dependsOn(common)

enablePlugins(JavaAppPackaging)

enablePlugins(DockerPlugin)

version in Docker := "latest"

resourceGenerators in Compile += Def.task {
  val file: File = (resourceManaged in Compile).value

  def allFiles(dir:File):Set[File] = {
    Option(dir.listFiles()).map(_.toList).getOrElse(List.empty).flatMap{
      case cDir if cDir.isDirectory =>
        allFiles(cDir)
      case cFile =>
        List(cFile)
    }.toSet
  }

  val curFiles = allFiles(file)

  (runner in Compile).value.run(
    "codacy.CheckResources", (dependencyClasspath in Compile).value.files, Array(file.getPath), streams.value.log
  )

  val newFiles = allFiles(file) -- curFiles

  newFiles.toList
}.taskValue

mappings in Universal <++= (resourceManaged, managedResources in Compile) map { (base:File, files:Seq[File]) =>
  files pair Path.rebase (base / "main", "")
}

val dockerUser = "docker"

daemonUser in Docker := dockerUser

dockerBaseImage := "frolvlad/alpine-oraclejdk8"

dockerCommands := dockerCommands.value.flatMap{
  case cmd@Cmd("WORKDIR","/opt/docker") => List(cmd,
    Cmd("USER","root"),
    Cmd("RUN","apk update && apk add bash")
  )
  case cmd@Cmd("ADD","opt /opt") => List(
    cmd,
    Cmd("RUN", s"adduser -u 2004 -D $dockerUser"),
    Cmd("RUN", "mv /opt/docker/docs /docs")
  )
  case other => List(other)
}
