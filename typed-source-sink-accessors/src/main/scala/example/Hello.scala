package example

object Hello extends App {
  println("Hello world")
  class Bus() {
    val data: Aligned[Int] = Aligned(1)
    val ready: Flipped[String] = Flipped("hi")
  }
  val src = Source(new Bus())
  val src_data: Source[Int] = src.data
  val src_ready: Sink[String] = src.ready
 
  println(src)
  println(src_data)
  println(src_ready)
}

