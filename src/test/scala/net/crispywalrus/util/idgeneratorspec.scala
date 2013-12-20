package net.crispywalrus.utils

import org.scalatest._

class IdGeneratorSpec extends FlatSpec {

  val gen = new IdImpl(0,0)

  "A generator" should "make new ids" in {
    val id: Long = gen.nextId
  }
  it should "generate unique ids" in {
    val results: Set[Long] = (0 to 9999).map({ _ => gen.nextId }).toSet
    assert( results.size == 10000 )
  }

}
