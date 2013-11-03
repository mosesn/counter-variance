package com.mosesn.counter_variance

import com.twitter.jsr166e.LongAdder

object Driver {
  def main(args: Array[String]) {
    val adder = new LongAdder()
    val adder2 = new LongAdder()
    for (i <- 0 until 10) {
      val t = new Thread(new Runnable {
        override def run() {
          while (true) {
            adder2.increment()
            adder.increment()
          }
        }
      })
      t.setDaemon(true)
      t.start()
    }

    for (i <- 0 until 60) {
      Thread.sleep(1000)
      println(adder.sum() - adder2.sum())
    }
  }
}
