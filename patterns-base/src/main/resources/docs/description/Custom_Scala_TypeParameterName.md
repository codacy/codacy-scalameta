Follows the Scala naming convention to use single letter type parameter.

If you prefer other naming conventions, you can change the 'regex' parameter to sooth your style.

A common practice is to use more descriptive names prefixed with a capital T.

For example:
```
    case class Entry[TKey, TProperties](key: TKey, properties: TProperties)
    
    trait HistoryBuilder[TKey, TEntity <: Entity[TProperties], TProperties] {
    ...
    }
```

In that case, set the regexp parameter to `^T[A-Z][A-Za-z0-9]*$`.

[More Info](http://docs.scala-lang.org/style/naming-conventions.html)
