//#Patterns: Custom_Scala_ReservedKeywords

//#Warn: Custom_Scala_ReservedKeywords
package object `package`{

    //#Warn: Custom_Scala_ReservedKeywords
    var `type` = "bad"

    var good = `type`
    //#Warn: Custom_Scala_ReservedKeywords
    def `def`() = ()
}

package object foo{

    //#Warn: Custom_Scala_ReservedKeywords
    type `import` = Int

    val t = 0
}

package docs{

    //#Warn: Custom_Scala_ReservedKeywords
    trait `trait`{}
    //#Warn: Custom_Scala_ReservedKeywords
    object `object`
    //#Warn: Custom_Scala_ReservedKeywords
    class `class`{ }
}