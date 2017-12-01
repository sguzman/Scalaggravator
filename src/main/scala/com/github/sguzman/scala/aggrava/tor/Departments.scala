package com.github.sguzman.scala.aggrava.tor

import java.net.HttpCookie

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.scraper.ContentExtractors.elementList

import scalaj.http.{Http, HttpResponse}

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
