package com.rocketlawyer.riak


import com.basho.riak.client._
import com.basho.riak.client.bucket._

object Riak {
  def fetchBucket(client:IRiakClient,name:String):Bucket = client.fetchBucket(name).execute
}
