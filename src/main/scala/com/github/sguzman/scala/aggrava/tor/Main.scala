package com.github.sguzman.scala.aggrava.tor

import java.net.HttpCookie

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._

import scalaj.http.{Http, HttpResponse}

object Main {
  def main(args: Array[String]): Unit = {
    val argv = Args(args)
    val lgn = Login(argv)

    val quarters = Quarters(lgn)
    println(quarters)

    val logins = quarters.map(_ => Login(argv))
    val departments = quarters.zip(logins).map(a => a._1 -> Departments(a._2))
    println(departments)
  }
}




