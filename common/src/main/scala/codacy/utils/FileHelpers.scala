package codacy.utils

import java.net.{URI, URL, URLClassLoader}

import better.files._

import scala.collection.JavaConversions._

object FileHelpers {

  implicit class UrlExtension(val value: URL) extends AnyVal {
    def toFile = fileFromUri(value.toURI)
  }

  private def fileFromUri(uri: URI): File = {
    def createFileSystemIfNotExists(): Unit = {
      try {
        val env = new java.util.HashMap[String, String]
        env.put("create", "true")
        java.nio.file.FileSystems.newFileSystem(uri, env)
      } catch {
        case _: java.nio.file.FileSystemAlreadyExistsException =>
      }
    }

    try {
      File(uri)
    } catch {
      case _: java.nio.file.FileSystemNotFoundException =>
        createFileSystemIfNotExists()
        fileFromUri(uri)
    }
  }

  def allPatternJsons(classLoader: URLClassLoader) = {
    classLoader.findResources("patterns.json").map(_.toFile).toSet
  }
}
