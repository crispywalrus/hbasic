package net.crispywalrus.legalgraph.entity

case class Error(code:Int,message:String)

case class Entity(id:Long,error:Error)
