Using the `withName` method will throw an Exception if the value does not exist in the Enumeration.
Your code should be ready to deal with the possibility of a value not existing.
Consider using `.values.find(_.toString == "MyValue")` instead of `.withName("MyValue")`, which returns an Option.

[More Info](https://stackoverflow.com/questions/23117681/how-to-read-string-into-enumeration-without-throwing-an-exception)
