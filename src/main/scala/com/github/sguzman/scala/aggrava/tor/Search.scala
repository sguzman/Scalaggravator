package com.github.sguzman.scala.aggrava.tor

import java.net.{HttpCookie, URLEncoder}
import java.nio.charset.StandardCharsets.UTF_8

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._

import scalaj.http.{Http, HttpRequest, HttpResponse}

object Search {
  def apply(quarter: String, department: String, response: HttpResponse[String]) = {
    val basicCoursePageRequest = basicCoursePage(response.cookies)

    val doc = JsoupBrowser().parseString(basicCoursePageRequest.asString.body)
    val hidden = doc >> elementList("""input[type="hidden"]""")
    val hiddenVals = hidden.map(s => List(s.attr("name"), s.attr("value")))
    val inputVals = List(
      List("ctl00%24pageContent%24quarterDropDown", quarter),
      List("ctl00%24pageContent%24subjectAreaDropDown", department),
      List("ctl00%24pageContent%24searchButton.x", "0"),
      List("ctl00%24pageContent%24searchButton.y", "0")
    )

    val bodyPairs = hiddenVals ++ inputVals
    val form = bodyPairs.map(s => s"${s.head}=${URLEncoder.encode(s(1), UTF_8.toString)}").mkString("&")

    val resp = basicCoursePageRequest.postData(form).asString
    resp
  }

  private def basicCoursePage(cookies: IndexedSeq[HttpCookie]): HttpRequest = {
    val url = "https://my.sa.ucsb.edu/gold/BasicFindCourses.aspx"
    Http(url)
      .header("Cookie", cookies.mkString("; "))
  }

  private def results(cookies: IndexedSeq[HttpCookie]): HttpResponse[String] = {
    val url = "https://my.sa.ucsb.edu/gold/ResultsFindCourses.aspx"
    val resp = Http(url).header("Cookie", cookies.mkString("; ")).asString
    resp
  }
}
