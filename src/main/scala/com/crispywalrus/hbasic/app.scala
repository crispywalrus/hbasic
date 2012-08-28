package com.crispywalrus.hbasic

import akka.actor._
import grizzled.slf4j.Logging
import unfiltered.netty.Http

import com.crispywalrus.hbasic.managers._

object HBasic extends App with Logging {

  info("starting HBasic hello server")
  info("creating actor system")
  val system = ActorSystem("HBasic")

  info("connecting to message queues")
  val queueManager = new QueueManager(system)
  queueManager.start match {
    case Right(t) => throw t
    case _ => {}
  }

  info("enabling web interface")
  val webManager = new WebManager(system)

  info("running HBasic")
  Http(1888).plan(webManager).run()
  info("HBasic complete")
}
