package com.github.sguzman.scala.aggrava.tor

import com.github.sguzman.scala.aggrava.tor.model.{Course, Department, Enroll, Quarter}
import com.github.sguzman.scala.aggrava.tor.retrieve.Retrieve

object Main {
  def main(args: Array[String]): Unit = {
    val raw = Retrieve(args)
    println(raw)

    val permutations = raw.flatMap(a => a._3.flatMap(b => b._2.map(c => (a._1, a._2, b._1, c))))
    println(permutations)

    val model = permutations.map(t => Quarter(t._1, Department(t._2, Course(t._3.mkString(" "), Enroll(t._4.mkString(" "))))))
    println(model)
  }
}




