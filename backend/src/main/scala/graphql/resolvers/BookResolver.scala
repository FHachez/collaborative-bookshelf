package graphql.resolvers

import java.time.LocalDate

import com.typesafe.scalalogging.LazyLogging
import graphql.types.{Book, BookTable}
import sangria.macros.derive._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class BookResolver() extends LazyLogging {

  def get(id: Long): Future[Option[Book]] = {
    BookTable.get(id)
  }

  @GraphQLField
  def add(title: String, subTitle: Option[String], authors: Seq[String],
    publisher: Option[String], publishedAt: Option[LocalDate], description: Option[String],
    categories: Seq[String], thumbnail: Option[String], language: Option[String], status: Option[String],
    goodReadsID: Option[String], goodReadsRatingsAvg: Option[Double],
    goodReadsRatingsCount: Option[Int]): Future[Boolean] = {

    if(BookTable.exists(title)) {
      return Future { false }
    }

    val book = new Book(
      -1, title, subTitle, authors, publisher, publishedAt, description, categories, thumbnail,
      language, status, goodReadsID, goodReadsRatingsAvg, goodReadsRatingsCount)

    BookTable.insert(book)

  }

  @GraphQLField
  def update(id: Long, title: String, subTitle: Option[String], authors: Seq[String],
             publisher: Option[String], publishedAt: Option[LocalDate], description: Option[String],
             categories: Seq[String], thumbnail: Option[String], language: Option[String], status: Option[String],
             goodReadsID: Option[String], goodReadsRatingsAvg: Option[Double],
             goodReadsRatingsCount: Option[Int]): Future[Boolean] = {

    if(!BookTable.exists(title)) {
      return Future { false }
    }

    val book = new Book(
      id, title, subTitle, authors, publisher, publishedAt, description, categories, thumbnail,
      language, status, goodReadsID, goodReadsRatingsAvg, goodReadsRatingsCount)

    BookTable.update(book)

  }

}