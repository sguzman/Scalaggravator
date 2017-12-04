package com.github.sguzman.scala.aggrava.tor.retrieve

object Retrieve {
  def apply(args: Array[String]): List[(String, String, List[(List[String], List[List[String]])])] = {
    val argv = Args(args)
    val lgn = Login(argv)

    val quarters = Quarters(lgn)
    val logins = quarters.par map (_ => Login(argv))
    val deptsByQuarts =
      quarters zip logins map (a => a._1 -> Departments(a._2)
        .filter(_.nonEmpty))

    val arguments = if (argv.debug)
      deptsByQuarts.filter(_._1 == "20174").flatMap(t => t._2.map((t._1, _)))
    else
      deptsByQuarts flatMap (t => t._2 map ((t._1, _)))

    val loginsForArgs = arguments.par map (_ => Login(argv))
    val searchArgs = arguments.zip(loginsForArgs)
      .map(t => (t._1._1, t._1._2, t._2))

    val results = searchArgs.map(t => Search(t._1, t._2, t._3))
    val resultResponses = loginsForArgs.par map (_.cookies) map Search.results

    val parsed = resultResponses.par map Split.apply
    val quartDeptCourses = arguments.zip(parsed).map(t => (t._1._1, t._1._2, t._2))
    quartDeptCourses
  }
}
