package com.github.sguzman.scala.aggrava.tor

import com.github.sguzman.scala.aggrava.tor.model.{Model, Quarter}
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
        Ok(Filter.byCourseJson(model, quarter, department, course))
      case GET at url"/$quarter/$department" =>
        Ok(Filter.byDepartmentJson(model, quarter, department))
      case GET at url"/$quarter" =>
        Ok(Filter.byQuarterJson(model, quarter))
      case GET at "/" =>
        Ok(json)
      case _ =>
        NotFound
    }
  }
}

object Filter {
  private def byQuarter(model: List[Quarter], quarter: String): List[Quarter] =
    model.filter(_.term.toLowerCase.contains(quarter.toLowerCase))

  def byQuarterJson(model: List[Quarter], quarter: String): String =
    new GsonBuilder().create.toJson(byQuarter(model, quarter).toArray)

  private def byDepartment(model: List[Quarter], quarter: String, department: String): List[Quarter] =
    byQuarter(model, quarter)
      .filter(_.departments.department.toLowerCase.contains(department.toLowerCase))

  def byDepartmentJson(model: List[Quarter], quarter: String, department: String): String =
    new GsonBuilder().create.toJson(byDepartment(model, quarter, department).toArray)

  private def byCourse(model: List[Quarter], quarter: String, department: String, courseName: String): List[Quarter] =
    byDepartment(model, quarter, department)
    .filter(_.departments.courses.course.toLowerCase.contains(courseName.toLowerCase))

  def byCourseJson(model: List[Quarter], quarter: String, department: String, courseName: String): String =
    new GsonBuilder().create.toJson(byCourse(model, quarter, department, courseName).toArray)
}