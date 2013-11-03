package com.mosesn.counter_variance

import com.twitter.jsr166e.LongAdder
import java.util.concurrent.locks.ReentrantReadWriteLock

object Driver {
  def main(args: Array[String]) {

    val lock = new ReentrantReadWriteLock()
    val adder = new LongAdder()
    val adder2 = new LongAdder()
    for (i <- 0 until 10) {
      val t = new Thread(new Runnable {
        override def run() {
          val read = lock.readLock()
          while (true) {
            read.lock()
            adder2.increment()
            adder.increment()
            read.unlock()
          }
        }
      })
      t.setDaemon(true)
      t.start()
    }

    println("without memory boundaries")
    for (i <- 0 until 60) {
      Thread.sleep(1000)
      println(adder.sum() - adder2.sum())
    }

    println("with memory boundaries")
    val write = lock.writeLock()
    for (i <- 0 until 60) {
      Thread.sleep(1000)
      write.lock()
      println(adder.sum() - adder2.sum())
      write.unlock()
    }
  }
}
