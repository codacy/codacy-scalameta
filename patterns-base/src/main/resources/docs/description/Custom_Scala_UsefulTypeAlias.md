Use type aliases when they provide convenient naming or clarify purpose, but do not alias types that are self-explanatory.

    () => Int

is clearer than

    type IntMaker = () => Int
    IntMaker

since it is both short and uses a common type. However

    class ConcurrentPool[K, V] {
      type Queue = ConcurrentLinkedQueue[V]
      type Map   = ConcurrentHashMap[K, Queue]
      ...
    }

is helpful since it communicates purpose and enhances brevity.

For more details:

[Effective Scala](https://twitter.github.io/effectivescala/#Types%20and%20Generics-Type%20aliases)
