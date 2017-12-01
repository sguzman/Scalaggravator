package com.github.sguzman.scala.aggrava.tor

import java.net.URLEncoder
import java.nio.charset.StandardCharsets.UTF_8

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._

import scala.util.{Failure, Success}
import scalaj.http.{Http, HttpRequest, HttpResponse}

object Main {
  def main(args: Array[String]): Unit = {
    val argv = Args(args)
    val lgn = Login(argv)
    println(lgn)
  }
}


