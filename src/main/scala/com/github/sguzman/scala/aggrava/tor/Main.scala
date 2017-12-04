package com.github.sguzman.scala.aggrava.tor

import com.github.sguzman.scala.aggrava.tor.model.Model
import com.github.sguzman.scala.aggrava.tor.retrieve.Retrieve
import com.google.gson.GsonBuilder
import lol.http._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object Main {
  def main(args: Array[String]): Unit = {
    val raw = Retrieve(args)
    val model = Model(raw)

    val gson = new GsonBuilder().create
    val json = gson.toJson(model.toArray)

    val port = util.Try(System.getenv("PORT").toInt) match {
      case Success(v) => v
      case Failure(e) => 9090
    }

    println("Ready Player 1")
    Server.listen(port) {
      case GET at url"/$quarter/$department/$course" =>
        wrapWithCORSHeaders(Ok(Filter.byCourseJson(model, quarter, department, course)))
      case GET at url"/$quarter/$department" =>
        wrapWithCORSHeaders(Ok(Filter.byDepartmentJson(model, quarter, department)))
      case GET at url"/$quarter" =>
        wrapWithCORSHeaders(Ok(Filter.byQuarterJson(model, quarter)))
      case GET at "/" =>
        wrapWithCORSHeaders(Ok(json))
      case _ =>
        NotFound
    }
  }

  def wrapWithCORSHeaders(ok: Response): Response = ok.addHeaders(
    (HttpString("Access-Control-Allow-Origin"), HttpString("*")),
    (HttpString("Access-Control-Allow-Headers"), HttpString("Origin, X-Requested-With, Content-Type, Accept"))
  )
}