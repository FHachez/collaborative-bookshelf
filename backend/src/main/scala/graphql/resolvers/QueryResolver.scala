package graphql.resolvers

import java.time.LocalDate

import com.typesafe.scalalogging.LazyLogging
import graphql.types.{ Book, BookTable }
import sangria.macros.derive._
import scalikejdbc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class QueryResolver(username: String) extends LazyLogging {

  @GraphQLField
  def book(id: String): Future[Book] = ???

}