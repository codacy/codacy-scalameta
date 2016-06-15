[![Codacy Badge](https://api.codacy.com/project/badge/grade/448773a482094001a1104979ca00350c)](https://www.codacy.com)

# Codacy scala.meta

This is the codacy engine for scala static code analysis build on top of scala.meta.
Check the **Docs** section for more information.

## Developing

implementing a new Pattern:

It is usually a good idea to start by implementing the test file. It contains issues the new pattern should detect as well as implementations that correct said issues. This defines the constraints of what the pattern should and also should not detect and helps avoid false positives.
To create a testfile add it to the resources under docs/tests/
You can take a look at the already implemented tests in our [base plugin](https://github.com/codacy/codacy-scalameta/tree/master/patterns-base/src/main/resources/docs/tests)

Next we implement the actual code. A pattern must currently reside in the package ```codacy.patterns```
and implement the ```codacy.base.Pattern``` interface. Which means it must implement the
```def apply(tree:Tree):Iterable[Result]```. In most cases the ```collect``` method on trees that scala.meta provides us with is sufficient for that purpose.
A ```Result``` consist of a ```Message``` (which itself is just a type-wrapped String) and a ```Positionable``` which can currently be implicitly obtained by passing a ```scala.meta.Tree``` or a ```scala.meta.Token```

Sometimes one might want to change the behavior of the pattern according to some configuration. Such a configuration has to follow some basic rules and gets passed to the pattern as the only argument in the constructor.
The rules are:

1) it needs to be a case class
2) all arguments need to define defaults
3) json readers and writers must exist for the argument types. (Currently that means primitive types and ```scala.util.matching.Regex```)

Additional steps can be found in the chapter Tool Integration with one exeption: there is no need to explicitly add pattern parameters to patterns defined in ```patterns.json``` since that's already being covered by rule 2 of the configuration rules.
For a full example check out the [existing patterns](https://github.com/codacy/codacy-scalameta/tree/master/patterns-base/src/main)

### Quasiquotes:
https://github.com/scalameta/scalameta/bob/master/notes/quasiquotes.md

### Tool Integration:
http://docs.codacy.com/v1.5/docs/tool-developer-guide

### Testing with "sbt console":

import scala.meta._

val code = """ .... """

val tree = code.parse[Source]

codacy.patterns.Custom_Scala_ElseIf(tree)

//To see the tree structure:

tree.show[Structure]

## Usage

You can create the docker by doing:

```
sbt docker:publishLocal
```

The docker is ran with the following command:

```
docker run -it -v $srcDir:/src  <DOCKER_NAME>:<DOCKER_VERSION>
```

## Docs

[Tool Developer Guide](http://docs.codacy.com/v1.5/docs/tool-developer-guide)

[Tool Developer Guide - Using Scala](http://docs.codacy.com/v1.5/docs/tool-developer-guide-using-scala)

## Test

We use the [codacy-plugins-test](https://github.com/codacy/codacy-plugins-test) to test our external tools integration.
You can follow the instructions there to make sure your tool is working as expected.
