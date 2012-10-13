package com.crispywalrus.hbasic.drivers

import scalaz._
import Scalaz._
import com.stackmob.scaliak._
import com.lambdaworks.jacks.JacksMapper._

import net.crispywalrus.drivers.riak._

trait Model {
  def flatten:DomainObject
  def inflate[M:Manifest](d:DomainObject):M = {
    readValue[M](d.data)
  }
}

case class User(key:String,firstName:String,lastName:String,email:String) extends Model {
  def flatten:DomainObject = new DomainObject(key,writeValueAsString(this))
}
