package net.crispywalrus.net.limit

import org.scalatest._

class FakeClock extends net.crispywalrus.utils.Clock {
  var fakeTime: Long = 0L

  def now = fakeTime

  def setTime(time: Long) {
    fakeTime = time
  }
}

class RelaxedBucketSpec extends WordSpec with Matchers {

  val sup = 1000000
  val maxSupportedRefillRate = 100
  val maxInterval = 1000

  // "A relaxed bucket" when {
  //   "contains n (n <= m) tokens" should {
  //     "provide m tokens" in {
  //       val mn0 = for { m <- choose(0, sup); n <- choose(m, sup) } yield (m, n)
  //       assert( true )
  //     }
  //   }
  // }
    // "where m > n, then I should get n tokens" ! {
    //   val mn0 = for { n <- choose(0, sup); m <- choose(n, sup) } yield (n, m)
    //   Prop.forAll(mn0) {
    //     case (n, m) =>
    //       TokenBucket.maker.relaxed.withTokens(n).make.tryConsume(m) must_== (TokenBucket.maker.withTokens(0).make, n)
    //   }
    // } ^
    // end ^
    // "Given a relaxed bucket having a capacity of m + n tokens and currently containing n tokens" ^
    // "when I try to deposit k + m tokens, where k >= 0, the bucket should contain (m+n) tokens" ! {
    //   val _mnk = for { m <- choose(0, sup); n <- choose(0, sup); k <- choose(0, sup) } yield (m, n, k)
    //   Prop.forAll(_mnk) {
    //     case (m, n, k) =>
    //       TokenBucket.maker.relaxed.withTokens(n).ofLimitedCapacity(m + n).make.deposit(k + m) must_==
    //         TokenBucket.maker.relaxed.withTokens(m + n).ofLimitedCapacity(m + n).make
    //   }
    // } ^
    // "when I try to deposit m - k tokens, where k > 0, the bucket should contain m + n - k tokens" ! {
    //   val _mnk = for { m <- choose(0, sup); n <- choose(0, sup); k <- choose(1, sup) } yield (m, n, k)
    //   Prop.forAll(_mnk) {
    //     case (m, n, k) =>
    //       TokenBucket.maker.relaxed.withTokens(n).ofLimitedCapacity(m + n).make.deposit(m - k) must_==
    //         TokenBucket.maker.relaxed.withTokens(m + n - k).ofLimitedCapacity(m + n).make
    //   }
    // } ^
    // end ^
    // "Given a relaxed bucket of capacity C with a refill rate of p tokens every s milliseconds" ^
    // "and given that m milliseconds ago the bucket contained i tokens and none have since been consumed" ^
    // "when I try to withdraw k tokens" ^
    // "where k <= C and k <= i + (m/s) * p, then I should get k tokens" ! {
    //   val clock = new FakeClock
    //   val params = for {
    //     c <- choose(0, sup)
    //     p <- choose(0, maxSupportedRefillRate)
    //     s <- choose(1, maxInterval)
    //     m <- choose(1, maxInterval)
    //     i <- choose(0, sup)
    //     k <- choose(0, math.min(c, i + (m / s) * p))
    //   } yield (c, p, s, m, i, k)
    //   Prop.forAllNoShrink(params) {
    //     case (c, p, s, m, i, k) => {
    //       val bucket = TokenBucket.maker.relaxed.ofLimitedCapacity(c).withTokens(i).suppliedBy(
    //         Tap.maker.usingClock(clock).make.adjustDropSize(p).adjustFrequency(s)).relaxed.make
    //       clock.setTime(m)
    //       bucket.tryConsume(k)._2 must_== k
    //     }
    //   }
    // } ^
    // "where k <= C and k > r = i + (m/s) * p, then I should get r tokens" ! {
    //   val clock = new FakeClock
    //   val params = for {
    //     c <- chooseNum(0, sup)
    //     p <- chooseNum(0, maxSupportedRefillRate)
    //     s <- chooseNum(1, maxInterval)
    //     m <- chooseNum(1, maxInterval)
    //     i <- chooseNum(0, sup)
    //     k <- chooseNum(1 + i + (m / s) * p, c)
    //     r <- i + (m / s) * p
    //   } yield (c, p, s, m, i, k, r)
    //   Prop.forAllNoShrink(params) {
    //     case (c, p, s, m, i, k, r) => {
    //       clock.setTime(0)
    //       val bucket = TokenBucket.maker.ofLimitedCapacity(c).withTokens(i).suppliedBy(
    //         Tap.maker.usingClock(clock).make.adjustDropSize(p).adjustFrequency(s)).relaxed.make
    //       clock.setTime(m)
    //       bucket.tryConsume(k)._2 must_== r
    //     }
    //   }
    // } ^
    // "where k > C and C <= i + (m/s) * p, then I should get C tokens" ! {
    //   val clock = new FakeClock
    //   val params = for {
    //     k <- chooseNum(1, sup)
    //     p <- chooseNum(0, maxSupportedRefillRate)
    //     s <- chooseNum(1, maxInterval)
    //     m <- chooseNum(1, maxInterval)
    //     i <- chooseNum(0, sup)
    //     c <- chooseNum(0, math.min(k - 1, i + (m / s) * p))
    //   } yield (c, p, s, m, i, k)
    //   Prop.forAllNoShrink(params) {
    //     case (c, p, s, m, i, k) => {
    //       clock.setTime(0)
    //       val bucket = TokenBucket.maker.ofLimitedCapacity(c).withTokens(i).suppliedBy(
    //         Tap.maker.usingClock(clock).make.adjustDropSize(p).adjustFrequency(s)).make
    //       clock.setTime(m)
    //       bucket.tryConsume(k)._2 must_== c
    //     }
    //   }
    // } ^
    // "where k > C and C > r = i + (m/s) * p, then I should get r tokens" ! {
    //   val clock = new FakeClock
    //   val params = for {
    //     k <- chooseNum(1, sup)
    //     p <- chooseNum(0, maxSupportedRefillRate)
    //     s <- chooseNum(1, maxInterval)
    //     m <- chooseNum(1, maxInterval)
    //     i <- chooseNum(0, sup)
    //     c <- chooseNum(1 + i + (m / s) * p, k - 1)
    //     r <- i + (m / s) * p
    //   } yield (c, p, s, m, i, k, r)
    //   Prop.forAllNoShrink(params) {
    //     case (c, p, s, m, i, k, r) => {
    //       clock.setTime(0)
    //       val bucket = TokenBucket.maker.ofLimitedCapacity(c).withTokens(i).suppliedBy(
    //         Tap.maker.usingClock(clock).make.adjustDropSize(p).adjustFrequency(s)).make
    //       clock.setTime(m)
    //       bucket.tryConsume(k)._2 must_== r
    //     }
    //   }
    // }
}
