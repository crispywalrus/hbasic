package net.crispywalrus.util

import org.specs2._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class IdGeneratorSpec extends SpecificationWithJUnit with ScalaCheck {

  def is = sequential ^
    "Given a generator, when I consume m id's" ! check {}

}
