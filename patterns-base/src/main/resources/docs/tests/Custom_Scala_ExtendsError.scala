//#Patterns: Custom_Scala_ExtendsError

//#Info: Custom_Scala_ExtendsError
class JavaErrorExt() extends java.lang.Error {

}

//#Info: Custom_Scala_ExtendsError
class ErrorExt() extends Error {

}

//#Info: Custom_Scala_ExtendsError
case class ErrorCaseExt() extends Error

//#Info: Custom_Scala_ExtendsError
case class JavaErrorCaseExt() extends java.lang.Error

class GoodClass() extends Any {
  // Compliant
}

class MyException extends Exception {
  // Compliant
}
