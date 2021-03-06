Scala has very expressive visibility modifiers. It’s important to use these as they define what constitutes the public API.
Public APIs should be limited so users don’t inadvertently rely on implementation details and limit the author’s ability
to change them: They are crucial to good modularity. As a rule, it’s much easier to expand public APIs than to contract
them. Poor annotations can also compromise backwards binary compatibility of your code.

    private[this]

A class member marked private,

    private val x: Int = ...

is visible to all instances of that class (but not their subclasses). In most cases, you want private[this].

    private[this] val x: Int = ...

which limits visibility to the particular instance. The Scala compiler is also able to translate private[this] into a
simple field access (since access is limited to the statically defined class) which can sometimes aid performance optimizations.

[Effective Scala](https://twitter.github.io/effectivescala/#Object%20oriented%20programming-Visibility)
