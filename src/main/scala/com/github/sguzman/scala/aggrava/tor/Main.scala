package com.github.sguzman.scala.aggrava.tor

import com.github.sguzman.scala.aggrava.tor.args.Args
import com.github.sguzman.scala.aggrava.tor.filter.CourseSplittify
import com.github.sguzman.scala.aggrava.tor.login.Login
import com.github.sguzman.scala.aggrava.tor.scrape.PostSearch
import com.github.sguzman.scala.ucsb.gold.miner.args.Args
import com.github.sguzman.scala.ucsb.gold.miner.filter.CourseSplittify
import com.github.sguzman.scala.ucsb.gold.miner.login.Login
import com.github.sguzman.scala.ucsb.gold.miner.scrape.{MetaScrape, PostSearch}

object Main {
  def main(args: Array[String]): Unit = {
    val argv = Args(args)
    Main(argv)
  }

  def apply(argv: Args): Unit = {
    apply(argv.user, argv.pass, argv.old)
  }

  def apply(user: String, pass: String, old: Boolean): Unit = {
    val argv = new Args
    argv.user = user
    argv.pass = pass
    argv.old = old

    val resp = Login.getUntilSome(argv)
    val (quarters, departments) = MetaScrape(resp)
    val arguments = departments flatMap (t => quarters map ((_, t)))
    arguments foreach println

    val logins = arguments.par.map(_ => Login.getUntilSome(argv)).toList
    logins foreach println

    val arguments2 = arguments.zip(logins).map(t => (t._1._1, t._1._2, t._2))
    arguments2 foreach println

    val courses = arguments2.par.map(i => PostSearch(i._1, i._2, i._3)).map(_.asString).toList
    courses foreach println

    val results = logins.par.map(PostSearch.results).map(_.asString)
    results foreach println

    val text = results.par.map(CourseSplittify.apply)
    text foreach println
  }
}
