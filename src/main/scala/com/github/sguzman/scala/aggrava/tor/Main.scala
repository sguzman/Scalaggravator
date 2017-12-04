package com.github.sguzman.scala.aggrava.tor

import com.github.sguzman.scala.aggrava.tor.model.Model
import com.github.sguzman.scala.aggrava.tor.retrieve.Retrieve

object Main {
  def main(args: Array[String]): Unit = {
    val raw = Retrieve(args)
    println(raw)

    val model = Model(raw)
    println(model)
  }
}




