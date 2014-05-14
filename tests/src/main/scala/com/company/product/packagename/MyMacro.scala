package com.company.product.packagename

import scala.language.experimental.macros
import scala.reflect.macros.Context
import scala.annotation.StaticAnnotation

class MyMacroAnnotation extends StaticAnnotation {
  def macroTransform(annottees: Any*) = macro myMacro.impl
}

object myMacro {
  def impl(c: Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {

    import c.universe._

    val (name, parent, traits, body) = annottees.map(_.tree).toList.collectFirst {
      case q"object $name extends $parent with ..$traits { ..$body }" =>
        (name, parent, traits, body)
    }.get

    val typeName = name.toTypeName
    val classType = c.typeCheck(q"(None.asInstanceOf[$typeName])").tpe

    // this has "illegal cyclic reference involving constructor" error if the class has default value for its member
    val fields: List[c.Symbol] = classType.declarations.collectFirst {
      case m: MethodSymbol if m.isPrimaryConstructor => m
    }.get.paramss.head

    val objFields = fields.map(field => q"val ${field.name.toTermName} = 5")

    c.Expr[Any](q"object $name extends $parent with ..$traits { ..${(objFields ++ body.asInstanceOf[List[Tree]]).toList} }")
  }
}