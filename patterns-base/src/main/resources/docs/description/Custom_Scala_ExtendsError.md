`java.lang.Error` should not be extended

`java.lang.Error` and its subclasses represent abnormal conditions, such as `OutOfMemoryError`, which should only be encountered by the Java Virtual Machine.

Noncompliant Code Example:

```
public class MyException extends Error { /* ... */ }       // Noncompliant
```

Compliant Solution:

```
public class MyException extends Exception { /* ... */ }   // Compliant
```

 [More Info](https://sbforge.org/sonar/rules/show/pmd:DoNotExtendJavaLangError?layout=false)
