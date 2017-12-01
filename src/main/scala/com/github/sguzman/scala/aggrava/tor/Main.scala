package com.github.sguzman.scala.aggrava.tor

import java.net.URLEncoder
import java.nio.charset.StandardCharsets.UTF_8

import com.github.sguzman.scala.aggrava.tor.args.Args
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._

import scala.util.{Failure, Success}
import scalaj.http.{Http, HttpRequest, HttpResponse}

object Main {
  def main(args: Array[String]): Unit = {
    val argv = Args(args)
    val lgn = Login(argv)

  }
}

object Login {
  private def getSome(req: HttpRequest): Option[HttpResponse[String]] =
    util.Try(req.asString) match {
      case Success(r) => if (r.body.contains("Error")) None else Some(r)
      case Failure(e) => None
    }

  private def getUntilSome(req: HttpRequest): HttpResponse[String] = {
    val resp = getSome(req)
    if (resp.isEmpty) getUntilSome(req)
    else resp.get
  }

  private def login(argv: Args): HttpRequest = {
    val url = "https://my.sa.ucsb.edu/gold/login.aspx"
    val resp = Http(url)

    val doc = JsoupBrowser().parseString(resp.asString.body)
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

  def apply(args: Args): HttpResponse[String] = {
    val lgn = login(args)
    val some = getUntilSome(lgn)

    some
  }
}
