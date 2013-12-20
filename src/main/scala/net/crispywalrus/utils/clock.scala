package net.crispywalrus.utils {

  trait Clock {
    def now: Long
  }

  class MilliClock extends Clock {
    def now = System.currentTimeMillis
  }

  object MilliClock extends MilliClock

  class NanoClock extends Clock {
    def now = System.nanoTime
  }

  object NanoClock extends NanoClock

}
