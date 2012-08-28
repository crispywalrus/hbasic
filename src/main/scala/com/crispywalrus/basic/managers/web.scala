package com.crispywalrus.basic

import akka.actor._
import grizzled.slf4j.Logging
import unfiltered.netty._
import unfiltered.request._
import unfiltered.response._

trait WebTrait extends cycle.Plan with cycle.ThreadPool with ServerErrorResponse

class WebManager(val system:ActorSystem) extends WebTrait with com.crispywalrus.basic.Manager with Logging {

  private [this] var isRunning:Boolean = false

  def running:Boolean = isRunning
  def start:Either[Unit,Throwable] = {
    isRunning = true
    new Left
  }
  def stop = { isRunning = false }

  def intent = {
    case _ => ResponseString("hello world")
 }

}
