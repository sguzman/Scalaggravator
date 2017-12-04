package com.github.sguzman.scala.aggrava.tor

import com.github.sguzman.scala.aggrava.tor.model.Quarter
import com.google.gson.GsonBuilder

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
