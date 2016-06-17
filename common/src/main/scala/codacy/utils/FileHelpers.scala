package codacy.utils

import java.net.{URI, URL, URLClassLoader}

import better.files._

import scala.util.{Failure, Success, Try}

import scala.collection.JavaConversions._

object FileHelpers {

  implicit class UrlExtension(val value:URL) extends AnyVal{
    def toFile = fileFromUri(value.toURI)
  }

  private def fileFromUri(uri:URI):File = {
    Try(File(uri)) match{
      case Success(file) => file
      case Failure(e:java.nio.file.FileSystemNotFoundException) =>
        val env = new java.util.HashMap[String,String]
        env.put("create", "true")
        Try(java.nio.file.FileSystems.newFileSystem(uri, env))
          .recover{ case _:java.nio.file.FileSystemAlreadyExistsException => () } match{
          case Success(_) => ()
          case Failure(err) => throw err
        }

        fileFromUri(uri)
      case Failure(other) =>
        throw other
    }
  }

  def allPatternJsons(classLoader: URLClassLoader) = {
    classLoader.findResources("patterns.json").map(_.toFile).toSet
  }
}
