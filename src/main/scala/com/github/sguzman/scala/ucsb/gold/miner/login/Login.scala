package com.github.sguzman.scala.ucsb.gold.miner.login

import java.net.URLEncoder
import java.nio.charset.StandardCharsets.UTF_8

import com.github.sguzman.scala.ucsb.gold.miner.args.Args
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._

import scalaj.http.{Http, HttpRequest, HttpResponse}

object Login {
  def get: HttpRequest = {
    val login = "https://my.sa.ucsb.edu/gold/login.aspx"
    Http(login)
  }

  def getSome(args: Args): Option[HttpResponse[String]] = {
    val resp = Login(args).asString
    if (resp.body.contains("Error")) None
    else Some(resp)
  }

  def getUntilSome(args: Args): HttpResponse[String] = {
    val resp = getSome(args)
    if (resp.isEmpty) getUntilSome(args)
    else resp.get
  }

  def apply(argv: Args): HttpRequest = {
    val loginUrl = "https://my.sa.ucsb.edu/gold/login.aspx"
    val resp = get

    val soup = JsoupBrowser()
    val doc = soup.parseString(resp.asString.body)
    val hidden = doc >> elementList("""input[type="hidden"]""")
    val hiddenVals = hidden.map(s => List(s.attr("name"), s.attr("value")))
    val inputVals = if (argv.old) List(
      List("ctl00%24pageContent%24PermPinLogin%24userNameText", argv.user),
      List("ctl00%24pageContent%24PermPinLogin%24passwordText", argv.pass),
      List("ctl00%24pageContent%24PermPinLogin%24loginButton.x", "0"),
      List("ctl00%24pageContent%24PermPinLogin%24loginButton.y", "0")
    ) else List(
      List("ctl00%24pageContent%24userNameText", argv.user),
      List("ctl00%24pageContent%24passwordText", argv.pass),
      List("ctl00%24pageContent%24loginButton.x", "0"),
      List("ctl00%24pageContent%24loginButton.y", "0")
    )

    val bodyPairs = hiddenVals ++ inputVals
    val form = bodyPairs.map(s => s"${s.head}=${URLEncoder.encode(s(1), UTF_8.toString)}").mkString("&")

    lazy val request = resp.postData(form)
    request
  }
}
