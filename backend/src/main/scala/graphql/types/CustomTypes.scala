package graphql.types

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import anorm.{ParameterMetaData, ToStatement}
import sangria.schema.ScalarType
import sangria.validation.ValueCoercionViolation
import sangria.ast

import scala.util.{Failure, Success, Try}

object CustomTypes {

  implicit val localDateToStatement = new ToStatement[LocalDate] {
    def set(s: java.sql.PreparedStatement, index: Int, aValue: LocalDate): Unit = {
      s.setDate(index, java.sql.Date.valueOf(aValue))
    }
  }

  implicit object LocalDateClassMetaData extends ParameterMetaData[LocalDate] {
    val sqlType = ParameterMetaData.DateParameterMetaData.sqlType
    val jdbcType = ParameterMetaData.DateParameterMetaData.jdbcType
  }

  case object DateCoercionViolation extends ValueCoercionViolation("Date value expected")

  private val localDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

  private def dateToString(date: LocalDate) = {
    date.format(localDateFormatter)
  }

  private def parseDateString(dateString: String) = {
    Try(LocalDate.parse(dateString, localDateFormatter)) match {
      case Success(date) ⇒ Right(date)
      case Failure(_) ⇒ Left(DateCoercionViolation)
    }
  }

  implicit val DateType = ScalarType[LocalDate](
    "Date",
    coerceOutput = (date, _) => dateToString(date),
    coerceUserInput = {
      case s: String => parseDateString(s)
      case _ => Left(DateCoercionViolation)
    },
    coerceInput = {
      case ast.StringValue(s, _, _, _, _) => parseDateString(s)
      case _ => Left(DateCoercionViolation)
    })

}

