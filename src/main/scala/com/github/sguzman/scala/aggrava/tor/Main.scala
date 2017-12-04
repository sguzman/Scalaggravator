package com.github.sguzman.scala.aggrava.tor

import com.github.sguzman.scala.aggrava.tor.model.Model
import com.github.sguzman.scala.aggrava.tor.retrieve.Retrieve
import com.google.gson.GsonBuilder
import lol.http._

import scala.util.{Failure, Success}

object Main {
  def main(args: Array[String]): Unit = {
    val raw = Retrieve(args)
    val model = Model(raw)

    val gson = new GsonBuilder().create
    val json = gson.toJson()

    val port = util.Try(System.getenv("PORT").toInt) match {
      case Success(v) => v
      case Failure(e) => 8888
    }

    println("Ready")
    Server.listen(port) {
      case GET at "/hello" =>
        Ok(json)
      case _ =>
        NotFound
    }
  }
}




