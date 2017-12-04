package com.github.sguzman.scala.aggrava.tor.model

object Model {
  type Raw = List[(String, String, List[(List[String], List[List[String]])])]

  def apply[A](raw: Raw): List[Quarter] = {
    val permutations = raw.flatMap(a => a._3.flatMap(b => b._2.map(c => (a._1, a._2, b._1, c))))

    val model = permutations.map(t =>
      Quarter(t._1,
        Department(t._2,
          Course(t._3.mkString(" "),
            Enroll(t._4.mkString(" "))))))

    model
  }
}
