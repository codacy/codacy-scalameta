[![Codacy Badge](https://api.codacy.com/project/badge/Grade/72869cd556c54624bcb36058c72d8371)](https://www.codacy.com/gh/codacy/codacy-scalameta?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=codacy/codacy-scalameta&amp;utm_campaign=Badge_Grade)
[![Build Status](https://circleci.com/gh/codacy/codacy-scalameta.svg?style=shield&circle-token=:circle-token)](https://circleci.com/gh/codacy/codacy-scalameta)

# Codacy scala.meta

This is the [codacy](https://www.codacy.com/) engine for scala static code analysis build on top of scala.meta.
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
https://github.com/scalameta/scalameta/blob/master/notes/quasiquotes.md

### Tool Integration:
https://docs.codacy.com/related-tools/tool-developer-guide/

### Testing with "sbt console":

```
import scala.meta._

val code = """ .... """

val tree = code.parse[Source]

codacy.patterns.Custom_Scala_ElseIf(tree)

//To see the tree structure:

tree.show[Structure]
```

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

[Tool Developer Guide](https://support.codacy.com/hc/en-us/articles/207994725-Tool-Developer-Guide)

[Tool Developer Guide - Using Scala](https://support.codacy.com/hc/en-us/articles/207280379-Tool-Developer-Guide-Using-Scala)

## Test

We use the [codacy-plugins-test](https://github.com/codacy/codacy-plugins-test) to test our external tools integration.
You can follow the instructions there to make sure your tool is working as expected.

## What is Codacy?

[Codacy](https://www.codacy.com/) is an Automated Code Review Tool that monitors your technical debt, helps you improve your code quality, teaches best practices to your developers, and helps you save time in Code Reviews.

### Among Codacy’s features:

- Identify new Static Analysis issues
- Commit and Pull Request Analysis with GitHub, BitBucket/Stash, GitLab (and also direct git repositories)
- Auto-comments on Commits and Pull Requests
- Integrations with Slack, HipChat, Jira, YouTrack
- Track issues in Code Style, Security, Error Proneness, Performance, Unused Code and other categories

Codacy also helps keep track of Code Coverage, Code Duplication, and Code Complexity.

Codacy supports PHP, Python, Ruby, Java, JavaScript, and Scala, among others.

### Free for Open Source

Codacy is free for Open Source projects.
