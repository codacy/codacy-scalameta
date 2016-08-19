package codacy.patterns

import codacy.base.Pattern

import scala.meta._

case object Custom_Scala_UnusedPrivateVariables extends Pattern {

  override def apply(tree: Tree) = {
    valDefsWithStats(tree).collect { case (currentName, stats) if !hasUsagesInStats(currentName, stats) =>
      Result(message(currentName), currentName)
    }
  }

  private[this] def hasUsagesInStats(name: Term.Name, stats: Seq[Tree]): Boolean = {
    stats.exists { tree =>
      tree.collect { case p"${currentName: Term.Name}"
        if name.toString == currentName.toString && name != currentName && !isTypeApply(currentName) && !isShadowed(currentName, name, stats) =>
        currentName
      }.nonEmpty
    }
  }

  private[this] def isTypeApply(name: Term.Name) = {
    name.parent.collect { case q"$expr[..$tpesnel]" => true }.exists(identity)
  }

  private[this] def isShadowed(name: Term.Name, definingName: Term.Name, stats: Seq[Tree]): Boolean = {
    def rec(treeOpt: Option[Tree]): Boolean = {
      treeOpt match {
        case None =>
          false

        case Some(tree: Tree) if hasParamOrValDefWithName(tree, definingName) =>
          true

        //did we reach the defining scope?
        case Some(tree) if stats.contains(tree) =>
          false

        case Some(tree) =>
          rec(tree.parent)
      }
    }

    rec(name.parent)
  }

  private[this] def hasParamOrValDefWithName(tree: Tree, name: Term.Name): Boolean = {
    //does the tree have a parameter with the given name?
    val paramWithName = tree.collect {
      case param"..$mods ${paramname: Term.Name}: $atpeopt = $expropt" if paramname != name =>
        paramname
    }
    //does the tree have a valDef with the given name?
    val valDefWithName = tree.collect {
      case q"..${mods: Seq[Mod]} val ..${patsnel: Seq[Pat]}: $tpeopt = $expr" =>
        patsnel.flatMap(_.collect { case n: Term.Name if n != name => n })
    }.flatten

    //does the tree have a defDef with the given name?
    val defDefWithName = tree.collect {
      case q"..$mods def ${name: Term.Name}[..$tparams](...$paramss): $tpeopt = $expr" =>
        name
    }

    (paramWithName ++ valDefWithName ++ defDefWithName).exists(_.toString() == name.toString())
  }

  private[this] def isPrivate(mods: Seq[Mod]) = mods.exists {
    case mod"private" => true
    case mod"private[this]" => true
    case _ => false
  }

  private[this] def isImplicit(mods: Seq[Mod]) = mods.exists {
    case mod"implicit" => true
    case _ => false
  }

  //return the name and the scope (seq[Tree] that can access it)
  private[this] def valDefsWithStats(tree: Tree): List[(Term.Name, Seq[Tree])] = {
    tree.collect {
      //valDefs
      case t@q"..${mods: Seq[Mod]} val ..${patsnel: Seq[Pat]}: $tpeopt = $expr" if isPrivate(mods) && !isImplicit(mods) =>
        //first parent should be the stats List[Tree], 2nd the class itself
        t.parent.flatMap(_.parent).map { classDef =>
          (classDef, patsnel.flatMap(_.collect { case p"${name: Term.Name}" => name }))
        }
      //paramVals
      case t@param"..$mods ${paramname: Term.Name}: $atpeopt = $expropt" if isPrivate(mods) && !isImplicit(mods) =>
        //first parent should be the constructor def, 2nd the class itself
        t.parent.flatMap(_.parent).map { classDef => (classDef, List(paramname)) }
    }.flatten
      .groupBy { case (template, _) => template }.map { case (template, lists) =>
      (template, lists.flatMap { case (_, names) => names })
    }.toList.flatMap { case (template, names) =>
      allStatsForName(template, names, tree)
    }
  }

  private[this] def allStatsForName(template: Tree, names: List[Term.Name], tree: Tree): List[(Term.Name, Seq[Tree])] = {
    val stats: Seq[Tree] = Option(template).collect {
      case classDef: Defn.Class =>
        classDef.templ.stats.getOrElse(Seq.empty) ++
          classDef.templ.early ++
          Option(classDef.ctor) ++
          companionStats(classDef.name, tree)

      case q"..$mods trait ${name: Type.Name}[..$tparams] extends $template" =>
        List(template) ++ companionStats(name, tree)

      case q"..$mods object $name extends $template" =>
        List(template)
    }.getOrElse(List.empty)

    names.map(_ -> stats)
  }

  private[this] def companionStats(name: Type.Name, tree: Tree): Option[Tree] = {
    tree.collect { case q"..$mods object ${oName: Term.Name} extends ${template: Tree}" if name.toString() == oName.toString =>
      template
    }.headOption
  }

  private[this] def message(name: Term.Name) = Message(s"Unused private variable.")
}