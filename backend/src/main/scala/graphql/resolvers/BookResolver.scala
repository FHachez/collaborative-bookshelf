package graphql.resolvers

import java.time.LocalDate

import com.typesafe.scalalogging.LazyLogging
import graphql.types.{ Book, BookTable }
import sangria.macros.derive._
import scalikejdbc._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class BookResolver(username: String) extends LazyLogging {

  @GraphQLField
  def add(title: String, subtitle: Option[String], authors: Seq[String],
    publisher: Option[String], publishedAt: Option[LocalDate], description: Option[String],
    categories: Seq[String], thumbnail: Option[String], language: Option[String], status: Option[String],
    goodReadsID: Option[String], goodReadsRatingsAvg: Option[Double],
    goodReadsRatingsCount: Option[Int]): Future[Boolean] = {

    val book = Book(
      title, subtitle, authors, publisher, publishedAt, description, categories, thumbnail,
      language, status, goodReadsID, goodReadsRatingsAvg, goodReadsRatingsCount)

    Future {
      val query =
        withSQL {
          insert.into(BookTable).values(
            book.productIterator.toList: _*)
        }

      logger.info(query.toString)

      DB.localTx { implicit session =>
        query.update.apply match {
          case 1 => true
          case _ => false
        }
      }
    }

  }

}