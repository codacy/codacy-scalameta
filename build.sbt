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
  .settings( (unmanagedClasspath in Compile) ++= Option((baseDirectory.value / ".." / "lib").listFiles()).toList.flatMap(_.toList) )
  .dependsOn(macros,basePatterns)

//the core that defines patterns and results
lazy val core = project.in(file("core"))
  .settings(scalaVersion := scalaV)

//our base package of scala patterns
lazy val basePatterns = project.in(file("patterns-base"))
  .settings(scalaVersion := scalaV)
  .dependsOn(core)

//the bridge between a codacy-engine and scala.meta patterns
lazy val common = project.in(file("common"))
  .dependsOn(core)
  .settings(scalaVersion := scalaV)
  .settings( libraryDependencies ++= Seq(
    Dependencies.codacyEngine,
    Dependencies.betterFiles
  ))

lazy val macros = project.in(file("macros")).
  settings(scalaVersion := scalaV).
  settings(libraryDependencies += Def.setting(scalaReflect).value).
  dependsOn(common)

enablePlugins(AshScriptPlugin)

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

  val result = (runner in Compile).value.run(
    "codacy.CheckResources", (dependencyClasspath in Compile).value.files ++ (unmanagedClasspath in Compile).value.files , Array(file.getPath), streams.value.log
  )

  //this ensures that compilation fails on erroneous resources!
  result.failed.foreach( r => throw new Exception(s"resource generation failed $r"))

  val newFiles = allFiles(file) -- curFiles

  newFiles.toList
}.taskValue

mappings in Universal ++= {
  val src = (resourceDirectory in Compile).value / "docs"
  val base = resourceManaged.value / "main"
  val files = (managedResourceDirectories in Compile).value.allPaths.get
  val dest = "/docs"

  val staticResources = for {
    path <- src.allPaths.get if !path.isDirectory
  } yield path -> path.toString.replaceFirst(src.toString, dest)

  staticResources ++
    (files pair Path.rebase (base, ""))
}

val dockerUser = "docker"

daemonUser in Docker := dockerUser

dockerBaseImage := "openjdk:8-jre-alpine"

dockerCommands := dockerCommands.value.flatMap{
  case cmd@Cmd("ADD",_) => List(
    Cmd("RUN", s"adduser -u 2004 -D $dockerUser"),
    cmd,
    Cmd("RUN", "mv /opt/docker/docs /docs")
  )
  case other => List(other)
}
