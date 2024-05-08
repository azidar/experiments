// See LICENSE.SiFive for license details.

// ******************** WARNING ********************
// This file (and any other file containing the keyword macro) needs to be as dependency-free as possible
// Scala incremental compilation is tracked at the file-level, and the macro keyword invalidates all
// downstream files if any upstream file changes at all (even adding a println inside of a function).
// **************************************************

package example

import scala.language.dynamics
import scala.language.experimental.macros

import example.macros.DynamicAccess

/** Interface to allow boxing of some underlying type will exporting the methods of that type
  *
  * This API should be used with care, it is intended for things like `ProtectedReg`.
  *
  * @example
  * {{{
  * class MyType[A <: Data] extends StructureDynamic[A] {
  *   // You must implement the select method even though it isn't a virtual method
  *   final def _select[B <: Data](f: A => B): MyType[B] = ???
  * }
  * }}}
  */
trait StructuredDynamic[A] extends Dynamic {

  final def selectDynamic(name: String): Any = macro DynamicAccess.select

  // Sadly this is needed because fizz.foo(0) is an apply dynamic, even if we wish it were selectDyanmic followed by apply
  final def applyDynamic(name: String)(args: Any*): Any = macro DynamicAccess.apply

  // Implementers of this class will need to implement the following method
  // We cannot define the virtual method here because subtypes have type bounds and it seems that
  // you cannot override a virtual method to a more concrete type when you have a type bound
  // Fortunately, because this is just a shell around macros, we can elide the virtual method
  // and things still work as long as we properly define _select in all subclasses
  // def _select[B](f: A => B): StructuredDynamic[B]
}
