package com.crispywalrus.hbasic.drivers

import scalaz._
import Scalaz._
import com.stackmob.scaliak._
import com.lambdaworks.jacks.JacksMapper._

class DomainObject(val key:String,val data:String)
object DomainObject {
  implicit val domainConverter: ScaliakConverter[DomainObject] = ScaliakConverter.newConverter[DomainObject](
    (o: ReadObject) => new DomainObject(o.key,o.stringValue).successNel,
    (o: DomainObject) => WriteObject(o.key,o.data.getBytes))
}

trait Model {
  def flatten:DomainObject
}

case class User(key:String,firstName:String,lastName:String,email:String) extends Model {
  def flatten:DomainObject = new DomainObject(key,writeValueAsString(this))
}
