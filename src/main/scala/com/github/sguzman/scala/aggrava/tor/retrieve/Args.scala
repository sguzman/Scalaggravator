package com.github.sguzman.scala.aggrava.tor.retrieve

import com.beust.jcommander.{JCommander, Parameter}
import org.feijoas.mango.common.base.Preconditions

private class Args {
  @Parameter(
    names = Array("-u", "--user", "--username"),
    description = "UCSB GOLD user name",
    arity = 1,
    echoInput = true,
    hidden = false,
    required = true,
    password = false,
    order = 1
  )
  var user: String = ""

  @Parameter(
    names = Array("-p", "--pass", "--password"),
    description = "UCSB GOLD password",
    arity = 1,
    echoInput = false,
    hidden = false,
    required = true,
    password = true,
    help = false,
    order = 2
  )
  var pass: String = ""

  @Parameter(
    names = Array("-o", "--old", "--oldfart"),
    description = "Is the account an old student?",
    arity = 0,
    echoInput = true,
    hidden = false,
    required = false,
    password = false,
    help = false,
    order = 3
  )
  var old: Boolean = false

  @Parameter(
    names = Array("-h", "--help", "--helpme"),
    description = "Show this menu",
    arity = 0,
    echoInput = true,
    hidden = false,
    required = false,
    password = false,
    help = true,
    order = 4
  )
  var help: Boolean = false

  @Parameter(
    names = Array("-d", "--debug"),
    description = "Debug mode",
    arity = 0,
    echoInput = true,
    hidden = true,
    required = false,
    password = false,
    help = false,
    order = 5
  )
  var debug: Boolean = false
}

private object Args {
  def apply(cmdArgs: Array[String]): Args = {
    val args = if (cmdArgs.length == 0) {
      val user = System.getenv("USER")
      val pass = System.getenv("PASS")
      val old = if (System.getenv("OLD") == "t") "-o" else ""
      Preconditions.checkArgument(user.nonEmpty, "Missing user var env")
      Preconditions.checkArgument(pass.nonEmpty, "Missing pass var env")
      Array("-u", user, "-p", pass, old)
    } else cmdArgs
    val argv = new Args
    val j = JCommander.newBuilder()
      .addObject(argv)
      .build()

    j.parse(args: _*)
    if (argv.help) {
      j.usage()
      System.exit(0)
    }

    argv
  }
}