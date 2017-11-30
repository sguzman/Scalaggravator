package com.github.sguzman.scala.aggrava.tor

import com.github.sguzman.scala.aggrava.tor.args.Args

import scalaj.http.Http

object Main {
  def main(args: Array[String]): Unit = {
    val argv = Args(args)
    lazy val lgn = login

  }

  def login = {
    println("login")
    val login = Http(url = "https://my.sa.ucsb.edu/gold/login.aspx")
    login
  }
}
