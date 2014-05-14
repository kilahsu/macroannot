package com.company.product.packagename


import org.scalatest._

// Not working if default value is used.
case class MyClass(name: String, value: Int = 3)
@MyMacroAnnotation object MyClass

class MyMacroSpec extends FlatSpec with Matchers  {

  "A test" should "pass" in {
    MyClass.name should be (5)
    MyClass.value should be (5)
  }
}
