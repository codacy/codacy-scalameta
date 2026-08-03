package codacy.patterns

import codacy.base.Pattern

import scala.meta._

case object Custom_Scala_UnusedPrivateVariables extends Pattern {

  override def apply(tree: Tree) = {
    valDefsWithStats(tree).collect {
      case (currentName, stats) if !hasUsagesInStats(currentName, stats) =>
        Result(message(currentName), currentName)
    }
  }

  private[this] def hasUsagesInStats(name: Term.Name, stats: Seq[Tree]): Boolean = {
    stats.exists { tree =>
      tree.collect {
        case p"${currentName: Term.Name}"
            if name.toString == currentName.toString && name != currentName && !isTypeApply(currentName) && !isShadowed(
              currentName,
              name,
              stats
            ) =>
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
    val paramWithName = tree.collect {
      case param: Term.Param if param.name != name =>
        param.name
    }

    val valDefWithName = tree.collect {
      case defn: Defn.Val =>
        defn.pats.flatMap(_.collect { case n: Term.Name if n != name => n })
      case defn: Defn.Var =>
        defn.pats.flatMap(_.collect { case n: Term.Name if n != name => n })
    }.flatten

    val defDefWithName = tree.collect {
      case defn: Defn.Def if defn.name != name =>
        defn.name
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
    tree
      .collect {
        //valDefs
        case t: Defn.Val if isPrivate(t.mods) && !isImplicit(t.mods) =>
          t.parent.flatMap(_.parent).map { classDef =>
            (classDef, extractNames(t.pats))
          }
        //varDefs
        case t: Defn.Var if isPrivate(t.mods) && !isImplicit(t.mods) =>
          t.parent.flatMap(_.parent).map { classDef =>
            (classDef, extractNames(t.pats))
          }
        //paramVals
        case t: Term.Param if isPrivate(t.mods) && !isImplicit(t.mods) =>
          t.parent.flatMap(_.parent).map { classDef =>
            (classDef, List(t.name))
          }
      }
      .flatten
      .groupBy { case (template, _) => template }
      .toList
      .flatMap {
        case (template, lists) =>
          val names: List[Term.Name] = lists.flatMap { case (_, ns) => ns }.asInstanceOf[List[Term.Name]]
          allStatsForName(template, names, tree)
      }
  }

  private[this] def allStatsForName(
      template: Tree,
      names: List[Term.Name],
      tree: Tree
  ): List[(Term.Name, Seq[Tree])] = {
    val stats: Seq[Tree] = Option(template)
      .collect {
        case classDef: Defn.Class =>
          classDef.templ.stats ++
            classDef.templ.early ++
            Option(classDef.ctor) ++
            companionStats(classDef.name, tree)

        case traitDef: Defn.Trait =>
          traitDef.templ.stats ++ companionStats(traitDef.name, tree)

        case objDef: Defn.Object =>
          objDef.templ.stats
      }
      .getOrElse(List.empty)

    names.map(_ -> stats)
  }

  private[this] def companionStats(name: Type.Name, tree: Tree): Option[Tree] = {
    tree.collect {
      case obj: Defn.Object if name.toString() == obj.name.toString =>
        obj.templ
    }.headOption
  }

  private[this] def extractNames(pats: Seq[Pat]): List[Term.Name] = {
    pats.flatMap {
      case Pat.Var(name) => List(name)
      case p"(..$inPats)" => extractNames(inPats)
      case _ => Nil
    }.toList
  }

  private[this] def message(name: Term.Name) = Message(s"Unused private variable.")
}
