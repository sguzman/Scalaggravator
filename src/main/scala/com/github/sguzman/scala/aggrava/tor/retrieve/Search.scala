package com.github.sguzman.scala.aggrava.tor.retrieve

import java.net.{HttpCookie, URLEncoder}
import java.nio.charset.StandardCharsets.UTF_8

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._

import scala.util.{Failure, Success}
import scalaj.http.{Http, HttpRequest, HttpResponse}

private object Search {
  def apply(quarter: String,
            department: String,
            response: HttpResponse[String],
            errorHandler: Any => Unit = Console.err.println): HttpResponse[String] = {
    util.Try({
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

      val basicPostResponse = basicCoursePageRequest.postData(form).asString
      val resp = basicCoursePage(response.cookies).asString
      resp
    }) match {
      case Success(v) => v
      case Failure(e) =>
        errorHandler(e.getMessage)
        apply(quarter, department, response, errorHandler)
    }
  }

  private def basicCoursePage(cookies: IndexedSeq[HttpCookie]): HttpRequest = {
    val url = "https://my.sa.ucsb.edu/gold/BasicFindCourses.aspx"
    Http(url)
      .header("Cookie", cookies.mkString("; "))
  }

  def results(cookies: IndexedSeq[HttpCookie]): HttpResponse[String] = {
    val url = "https://my.sa.ucsb.edu/gold/ResultsFindCourses.aspx"
    val resp = Http(url).header("Cookie", cookies.mkString("; ")).asString
    resp
  }
}
