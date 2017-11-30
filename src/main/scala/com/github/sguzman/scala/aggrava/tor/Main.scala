package com.github.sguzman.scala.aggrava.tor

import com.github.sguzman.scala.aggrava.tor.args.Args

object Main {
  def main(args: Array[String]): Unit = {
    val argv = Args(args)
    println(argv)
  }
}
