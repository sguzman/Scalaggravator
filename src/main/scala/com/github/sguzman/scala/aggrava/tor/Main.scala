package com.github.sguzman.scala.aggrava.tor

object Main {
  def main(args: Array[String]): Unit = {
    val argv = Args(args)
    val lgn = Login(argv)

    val quarters = Quarters(lgn)
    println(quarters)

    val logins = quarters.map(_ => Login(argv))
    val departments = quarters.zip(logins).map(a => a._1 -> Departments(a._2).filter(_.nonEmpty))
    println(departments)


  }
}




