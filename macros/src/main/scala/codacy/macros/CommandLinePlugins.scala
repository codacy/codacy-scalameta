package codacy.macros

import java.net.URLClassLoader

import codacy.CommandLinePlugin
import codacy.utils.FileHelpers._

import scala.collection.JavaConversions._
import scala.reflect.macros.whitebox._

object CommandLinePlugins {
  import scala.language.experimental.macros
  def list:List[CommandLinePlugin] = macro CommandLinePluginsMacro.impl
}

private[macros] object CommandLinePluginsMacro{

  def impl(context: Context): context.Expr[List[CommandLinePlugin]] = {
    import context.universe._

    val plugins = new URLClassLoader(context.classPath.toArray).findResources("codacy-engine-plugin").toList.map{ case uri =>
      val fullName = uri.toFile.contentAsString
      val Plugin = context.mirror.staticModule(fullName)

      q"${Plugin.asTerm}"
    }

    context.Expr[List[CommandLinePlugin]](
      q"List( ..$plugins )"
    )
  }
}