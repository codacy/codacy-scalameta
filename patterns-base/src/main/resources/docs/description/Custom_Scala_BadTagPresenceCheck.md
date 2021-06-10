FIXME tags are commonly used to mark places where a bug is suspected, but which the developer wants to deal with later.

Sometimes the developer will not have the time or will simply forget to get back to that tag.

This rule is meant to track those tags and to ensure that they do not go unnoticed.

Noncompliant Code Example

    int divide(int numerator, int denominator) {
      return numerator / denominator;              // FIXME denominator value might be  0
    }

For more details:

[Sonarqube](https://rules.sonarsource.com/java/RSPEC-1134)
