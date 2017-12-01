package com.github.sguzman.scala.aggrava.tor

import com.beust.jcommander.{JCommander, Parameter}

class Args {
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
}

object Args {
  def apply(args: Array[String]): Args = {
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