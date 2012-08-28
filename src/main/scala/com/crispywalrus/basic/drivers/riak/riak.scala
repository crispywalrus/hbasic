package com.crispywalrus.basic.drivers.riak

import akka.actor._
import com.stackmob.scaliak._
import com.basho.riak.client._
import bucket._
import com.lambdaworks.jacks.JacksMapper._
import scalaz._
import Scalaz._

import com.crispywalrus.basic.drivers._

class Riak(val host:String,val port:Tuple2[Option[Int],Option[Int]]=Tuple2(Some(8098),None))
{
  val client = port._2 match {
    case None => Scaliak.httpClient(host)
    case Some(p) => Scaliak.pbClient(host,p,port._1 match {
      case None => 8098
      case Some(h) => h
    })
  }

}

class RiakBucket(val name:String,server:Riak)
{

  val bucket:ScaliakBucket = server.client.bucket(name).unsafePerformIO match {
    case Success(b) => b
    case Failure(e) => throw e
  }

  def put(model:DomainObject):Unit =
    if( bucket.store(model).unsafePerformIO.isFailure )
      throw new Exception("unable to store "+model.key+" in bucket "+name)

  def fetch(key:String):Option[DomainObject] = {
    bucket.fetch(key).unsafePerformIO match {
      case Success(roo) => {
        roo match {
          case None => None
          case Some(ro) => Some(new DomainObject(ro.key,ro.stringValue))
        }
      }
      case Failure(es) => throw es.head
    }
  }

}

