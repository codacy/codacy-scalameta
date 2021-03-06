"indexOf" checks should not be for positive numbers

Most checks against an `indexOf` value compare it with `-1` because `0` is a valid index. Any checks which look for values `> 0` ignore the first element, which is likely a bug. If the intent is merely to check inclusion of a value in a `String` or a `List`, consider using the `contains` method instead.

For strings, if the intent is truly to find the first index after a specific character index, then use the version of `indexOf` which takes a start position argument.

This rule raises an issue any time an `indexOf` value retrieved either from a `String` or a `List` is tested for a positive value.

Noncompliant Code Example:

    String color = "blue";
    String name = "ishmael";

    List<String> strings = new ArrayList<String> ();
    strings.add(color);
    strings.add(name);

    if (strings.indexOf(color) > 0) {  // Noncompliant
      // ...
    }
    if (name.indexOf("ish") > 0) { // Noncompliant
      // ...
    }
    if (name.indexOf("ae") > 0) { // Noncompliant
      // ...
    }

Compliant Solution:

    String color = "blue";
    String name = "ishmael";

    List<String> strings = new ArrayList<String> ();
    strings.add(color);
    strings.add(name);

    if (strings.indexOf(color) > -1) {
      // ...
    }
    if (name.indexOf("ish") >= 0) {
      // ...
    }
    if (name.contains("ae") {
      // ...
    }

[Sonarqube](https://rules.sonarsource.com/java/RSPEC-2692)
