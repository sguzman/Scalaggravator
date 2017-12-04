package com.github.sguzman.scala.aggrava.tor

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import org.apache.commons.lang3.StringUtils

import scalaj.http.HttpResponse

object Split {
  def apply(resp: HttpResponse[String]) = {
    val body = resp.body
    val doc = JsoupBrowser().parseString(body)

    val courses = doc >> elementList("#pageContent_CourseList > tbody > tr")
    val courseIds = courses map (_ >> elementList("[id]"))
    val text = courseIds map (_ map (_.text))

    val numCourses = text map (t => (t.count(_.isEmpty) / 2, t)) filter (_._1 != 0) filter (_._2.length > 4)
    val courseDesc = numCourses.map(l =>
      StringUtils.substringBefore(l._2.head, "    ").trim :: l._2(2) :: l._2(3) :: l._1.toString :: Nil)
    val splitByRow = numCourses map splitRow
    val splitByColumn = splitByRow map (_ map splitColumn)

    val attachCourseDesc = courseDesc.zip(splitByColumn)
    attachCourseDesc
  }

  def splitRow(tup: (Int, List[String])): List[String] = {
    val numOfRows = tup._1
    val courseTexts = tup._2
    val rowsInText = courseTexts(4)

    if (numOfRows == 1) {
      List(rowsInText)
    } else {
      val enrollCodePattern = """[1-9][0-9][0-9][0-9][0-9]"""
      val enrollCodes = enrollCodePattern.r.findAllMatchIn(rowsInText).toList
      val splitAtRegex = rowsInText.split(enrollCodePattern).tail
      val addBackEnrollCode = enrollCodes.zip(splitAtRegex).map(t => t._1 + t._2)
      addBackEnrollCode
    }
  }

  def splitColumn(alist: String): List[String] = {
    val split = alist.split("\\s+")
    val trimmed = split map (_.trim)
    val toList = trimmed.toList

    toList
  }
}
