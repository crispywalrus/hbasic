package com.crispywalrus.hbasic.managers

trait Manager {
  def start:Either[Unit,Throwable]
  def stop:Unit
  def running:Boolean
}
