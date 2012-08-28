package com.crispywalrus.hbasic.managers

import akka.actor._
import com.rabbitmq.client.ConnectionFactory
import grizzled.slf4j.Logging

import com.crispywalrus.hbasic.driver.amqp._
import Amqp._

class QueueManager(val system:ActorSystem) extends Manager with Logging {

  private [this] var isRunning:Boolean = false

  def running:Boolean = isRunning
  def start:Either[Unit,Throwable] = {
    isRunning = true
    new Left
  }

  def stop = { isRunning = false }

  // rabbitmq 
  def connectionFactory = new ConnectionFactory
  val connectionManager = system.actorOf(Props(new ConnectionOwner(connectionFactory)),name="rabbit")

}
