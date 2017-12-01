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

object Departments {
  private def get(cookies: IndexedSeq[HttpCookie]): HttpResponse[String] = {
    val url = "https://my.sa.ucsb.edu/gold/BasicFindCourses.aspx"
    Http(url)
      .header("Cookie", cookies.mkString("; ")).asString
  }

  def apply(response: HttpResponse[String]): List[String] = {
    val id = "pageContent_subjectAreaDropDown"
    val basicFindCoursesPage = get(response.cookies)
    val doc = JsoupBrowser().parseString(basicFindCoursesPage.body)
    val menu = doc >> elementList(s"#$id > option")
    menu map (_ attr "value")
  }
}


