package codacy

import codacy.dockerApi._
import codacy.macros.CommandLinePlugins

object Engine extends DockerEngine(CodacyScalameta){

  override def main(args: Array[String]): Unit = {
    //if no plugin applies just use the default behaviour
    lazy val defaultPlugin = new CommandLinePlugin({ case args => super[DockerEngine].main(args)})
    val plugins: List[CommandLinePlugin] = CommandLinePlugins.list

    plugins.
      find{ case plugin:CommandLinePlugin => plugin.main.isDefinedAt(args)}.
      getOrElse(defaultPlugin).
      main( args )
  }
}