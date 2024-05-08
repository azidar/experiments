// See LICENSE.SiFive for license details.

package example.macros

import scala.reflect.macros.whitebox

/** Macro to rewrite apply calls as calls to `selectDynamic` with the val name
  * provided as the select argument and an implicit for the singleton type.
  */
object DynamicAccess {
  // Rewrite foo.bar into foo._select(_.bar)
  def select(c: whitebox.Context)(name: c.Tree): c.Tree = {
    import c.universe._
    val rawName            = name.toString
    // rawName is surrounded by double quotes, remove them
    val stripped           = rawName.substring(1, rawName.length - 1)
    val termName: TermName = TermName(stripped)
    q"${c.prefix.tree}._select(_.$termName)"
  }

  // Rewrite foo.bar(0) into foo._select(_.bar).apply(0)
  def apply(c: whitebox.Context)(name: c.Tree)(args: c.Tree*): c.Tree = {
    import c.universe._
    val rawName            = name.toString
    // rawName is surrounded by double quotes, remove them
    val stripped           = rawName.substring(1, rawName.length - 1)
    val termName: TermName = TermName(stripped)
    q"${c.prefix.tree}._select(_.$termName).apply(...$args)"
  }
}
