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
  .settings(
    resourceGenerators in Compile += Def.task {
      val dest: File = (resourceManaged in Compile).value / "patterns"
      val src = (scalaSource in Compile).value / "codacy" / "patterns"

      val all = for {
        path <- src.listFiles().toList if path.toString.endsWith(".scala")
      } yield path -> dest / path.name

      IO.copy(all).toList
    }.taskValue
  )

//the bridge between a codacy-engine and scala.meta patterns
lazy val common = project.in(file("common"))
  .dependsOn(core)
  .settings(scalaVersion := scalaV)
  .settings( libraryDependencies ++= Seq(
    Dependencies.codacyEngine,
    Dependencies.playJson,
    Dependencies.betterFiles
  ))

lazy val macros = project.in(file("macros")).
  settings(scalaVersion := scalaV).
  settings(libraryDependencies += Def.setting(scalaReflect).value).
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

  val result = (runner in Compile).value.run(
    "codacy.CheckResources", (dependencyClasspath in Compile).value.files ++ (unmanagedClasspath in Compile).value.files , Array(file.getPath), streams.value.log
  )

  //this ensures that compilation fails on erroneous resources!
  result.foreach( r => throw new Exception(s"resource generation failed $r"))

  val newFiles = allFiles(file) -- curFiles

  newFiles.toList
}.taskValue

mappings in Universal <++= (resourceDirectory in Compile, resourceManaged, managedResources in Compile) map {
  (resourceDir: File, base:File, files:Seq[File]) =>
    val src = resourceDir / "docs"
    val dest = "/docs"

    val staticResources = for {
      path <- src.***.get
      if !path.isDirectory
    } yield path -> path.toString.replaceFirst(src.toString, dest)

    staticResources ++
      (files pair Path.rebase (base / "main", ""))
}

val dockerUser = "docker"

daemonUser in Docker := dockerUser

dockerBaseImage := "frolvlad/alpine-oraclejdk8:cleaned"

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
