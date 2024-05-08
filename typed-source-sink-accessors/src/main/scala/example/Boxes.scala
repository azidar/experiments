package example

trait BoxLike
sealed trait Box[T] extends StructuredDynamic[T] with BoxLike {
  def proto: T
}
trait SelectReturn[B <: BoxLike, T, S] {
  type R
  def wrap(ret: S, box: B): R
}
object SelectReturn {
  type SelectReturnType[B <: BoxLike, T, S, X] = SelectReturn[B, T, S] { type R = X}
  implicit def returnAlignedFromSource[T, S]: SelectReturnType[Source[T], T, Aligned[S], Source[S]] = new SelectReturn[Source[T], T, Aligned[S]] {
    type R = Source[S]
    def wrap(ret: Aligned[S], box: Source[T]): Source[S] = Source(ret.proto)
  }
  implicit def returnFlippedFromSource[T, S]: SelectReturnType[Source[T], T, Flipped[S], Sink[S]] = new SelectReturn[Source[T], T, Flipped[S]] {
    type R = Sink[S]
    def wrap(ret: Flipped[S], box: Source[T]): Sink[S] = Sink(ret.proto)
  }
  implicit def returnAlignedFromSink[T, S]: SelectReturnType[Sink[T], T, Aligned[S], Sink[S]] = new SelectReturn[Sink[T], T, Aligned[S]] {
    type R = Sink[S]
    def wrap(ret: Aligned[S], box: Sink[T]): Sink[S] = Sink(ret.proto)
  }
  implicit def returnFlippedFromSink[T, S]: SelectReturnType[Sink[T], T, Flipped[S], Source[S]]  = new SelectReturn[Sink[T], T, Flipped[S]] {
    type R = Source[S]
    def wrap(ret: Flipped[S], box: Sink[T]): Source[S] = Source(ret.proto)
  }
}

case class Type[T] (proto: T) extends Box[T] {
  def _select[S](f: T => S)(implicit sr: SelectReturn[Type[T], T, S]): sr.R = {
    val selected = f(proto)
    sr.wrap(selected, this)
  }
}
case class Aligned[T] (proto: T) extends Box[T] {
  def _select[S](f: T => S)(implicit sr: SelectReturn[Aligned[T], T, S]): sr.R = {
    val selected = f(proto)
    sr.wrap(selected, this)
  }
}
case class Flipped[T] (proto: T) extends Box[T] {
  def _select[S](f: T => S)(implicit sr: SelectReturn[Flipped[T], T, S]): sr.R = {
    val selected = f(proto)
    sr.wrap(selected, this)
  }
}
case class Source[T] (proto: T) extends Box[T] {
  def _select[S, X](f: T => S)(implicit sr: SelectReturn.SelectReturnType[Source[T], T, S, X]): X = {
    val selected = f(proto)
    sr.wrap(selected, this)
  }
}
case class Sink[T] (proto: T) extends Box[T] {
  def _select[S, X](f: T => S)(implicit sr: SelectReturn.SelectReturnType[Sink[T], T, S, X]): X = {
    val selected = f(proto)
    sr.wrap(selected, this)
  }
}