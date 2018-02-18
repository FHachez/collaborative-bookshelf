package graphql.resolvers

import java.time.LocalDate

import com.typesafe.scalalogging.LazyLogging
import graphql.types.{ Book, BookTable }
import sangria.macros.derive._
import scalikejdbc._

import scala.concurrent.Future

class BookResolver(username: String) extends LazyLogging {

  implicit val session = AutoSession

  @GraphQLField
  def add(title: String, subTitle: Option[String], authors: Seq[String],
    publisher: Option[String], publishedAt: Option[LocalDate], description: Option[String],
    categories: Seq[String], thumbnail: Option[String], language: Option[String], status: Option[String],
    goodReadsID: Option[String], goodReadsRatingsAvg: Option[Double],
    goodReadsRatingsCount: Option[Int]): Future[Boolean] = {

    val book = Book(
      title, subTitle, authors, publisher, publishedAt, description, categories, thumbnail,
      language, status, goodReadsID, goodReadsRatingsAvg, goodReadsRatingsCount)

    BookTable.insert(book)

  }

}