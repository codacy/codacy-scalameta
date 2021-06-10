Don't repeat names that are already encapsulated in package or object name

Prefer:

    object User {
      def get(id: Int): Option[User]
    }

to

    object User {
      def getUser(id: Int): Option[User]
    }

They are redundant in use: `User.getUser` provides no more information than `User.get`.

[Effective Scala](https://twitter.github.io/effectivescala/#Formatting-Naming)
