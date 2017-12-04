package com.github.sguzman.scala.aggrava.tor.retrieve

import java.net.HttpCookie

import net.ruippeixotog.scalascraper.browser.{Browser, JsoupBrowser}
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.scraper.ContentExtractors.elementList

import scalaj.http.{Http, HttpRequest, HttpResponse}

private object Quarters {
  def apply(req: HttpResponse[String]): List[String] = {
    val url = "https://my.sa.ucsb.edu/gold/BasicFindCourses.aspx"
    val basicFindCourses = get(req.cookies).asString
    val doc = JsoupBrowser().parseString(basicFindCourses.body)

    val quarts = quarters(doc)
    quarts
  }

  private def get(cookies: IndexedSeq[HttpCookie]): HttpRequest = {
    val url = "https://my.sa.ucsb.edu/gold/BasicFindCourses.aspx"
    Http(url)
      .header("Cookie", cookies.mkString("; "))
  }

  private def quarters(doc: Browser#DocumentType): List[String] = {
    val id = "pageContent_quarterDropDown"
    val menu = doc >> elementList(s"#$id > option")

    val values = menu.map(_.attr("value"))
    values
  }
}
