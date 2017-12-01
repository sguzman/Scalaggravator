package com.github.sguzman.scala.aggrava.tor

import java.net.URLEncoder

import com.github.sguzman.scala.aggrava.tor.args.Args
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._

import scalaj.http.{Http, HttpRequest}

object Main {
  def main(args: Array[String]): Unit = {
    val argv = Args(args)
    val lgn = Login(argv)

  }
}

object Login {
  def apply(argv: Args): HttpRequest = {
    println("login")
    val url = "https://my.sa.ucsb.edu/gold/login.aspx"
    val resp = Http(url)

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

    val request = resp.postData(form)
    request
  }
}
