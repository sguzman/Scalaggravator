package com.github.sguzman.scala.aggrava.tor

object Main {
  def main(args: Array[String]): Unit = {
    val argv = Args(args)
    val lgn = Login(argv)

    val quarters = Quarters(lgn)
    println(quarters)

    val logins = quarters.par map (_ => Login(argv))
    val deptsByQuarts =
      quarters zip logins map (a => a._1 -> Departments(a._2)
        .map(_.trim)
        .filter(_.nonEmpty))
    println(deptsByQuarts)

    val arguments = if (argv.debug)
      deptsByQuarts.filter(_._1 == "20174").flatMap(t => t._2.map((t._1, _)))
    else
      deptsByQuarts flatMap (t => t._2 map ((t._1, _)))
    println(arguments)

    val loginsForArgs = arguments.par map (_ => Login(argv))
    println(loginsForArgs)
  }
}




