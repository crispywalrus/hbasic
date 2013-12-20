package net.crispywalrus.utils {

  import grizzled.slf4j.Logging

  trait IdGenerator {
    def nextId: Long
  }

  /**
    * generate a 64 bit unique ID. each id is a lamport timestamp,42
    * bits of time,8 bits of worker id,2 bits of cluster id and a 12 bit
    * sequence counter. This gives this class an effective lifespan of 140 years
    * and allows for 8k timestamps per millisecond.
    *
    */
  class IdImpl(val workerId: Long,val clusterId: Long) extends IdGenerator with Logging {

    val epoch = 1041654066000L // start epoch at the birth of Stella Celeste Vale,January 3,2003 21:21 PST

    val workerIdBits = 8L
    val clusterIdBits = 2L
    val maxWorkerId = -1L ^(-1L << workerIdBits)
    val maxClusterId = -1L ^(-1L << clusterIdBits)
    val sequenceBits = 12L

    val workerIdShift = sequenceBits
    val clusterIdShift = sequenceBits+workerIdBits
    val timestampLeftShift = sequenceBits+workerIdBits+clusterIdBits
    val sequenceMask = -1L ^(-1L << sequenceBits)

    var lastTimestamp = -1L
    var sequence: Long = 0L

    // sanity check for workerId
    if(workerId > maxWorkerId || workerId < 0) {
      throw new IllegalArgumentException("worker Id can't be greater than %d or less than 0".format(maxWorkerId))
    }

    if(clusterId > maxClusterId || clusterId < 0) {
      throw new IllegalArgumentException("cluster Id can't be greater than %d or less than 0".format(maxClusterId))
    }

    info("worker starting. timestamp left shift %d,cluster id bits %d,worker id bits %d,sequence bits %d,workerid %d",
         timestampLeftShift,clusterIdBits,workerIdBits,sequenceBits,workerId)

    def nextId(): Long = synchronized {
      var timestamp = timeGen()

      // if the milli clock hasn't rolled since the last id was generated...
      if(lastTimestamp == timestamp) {
        // yes,increment the counter
        sequence =(sequence+1) & sequenceMask
        if(sequence == 0) {
          // unless we get a roll over,the wait till next milli
          timestamp = tilNextMillis(lastTimestamp)
        }
      } else if(timestamp > lastTimestamp) {
        // else new milli,reset sequence counter
        sequence = 0
      } else {
        // current millis is less than previous millis,bitch about it
        error("clock is moving backwards.  Rejecting requests until %d.".format(lastTimestamp));
        throw new RuntimeException("Clock moved backwards.  Refusing to generate id for %d milliseconds".format(lastTimestamp - timestamp))
      }

      // for next timestamp
      lastTimestamp = timestamp

      // nasty bit match to make a 64 bit number out of all our components
     ((timestamp - epoch) << timestampLeftShift) |
     (clusterId << clusterIdShift) |
     (workerId << workerIdShift) |
      sequence
    }

    protected def tilNextMillis(lastTimestamp: Long): Long = {
      var timestamp = timeGen()
      while(timestamp <= lastTimestamp) {
        timestamp = timeGen()
      }
      timestamp
    }

    protected def timeGen(): Long = System.currentTimeMillis()

  }

}
