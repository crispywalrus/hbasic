package com.crispywalrus.basic

trait Manager {
  def start:Either[Unit,Throwable]
  def stop:Unit
  def running:Boolean
}
