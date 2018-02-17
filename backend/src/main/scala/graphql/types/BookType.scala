package graphql.types

import java.time.LocalDate

import graphql.resolvers.{ BookResolver, RootResolver }
import CustomTypes._
import sangria.macros.derive._
import scalikejdbc._

case class Book(title: String, subtitle: Option[String], authors: Seq[String],
  publisher: Option[String], publishedAt: Option[LocalDate], description: Option[String],
  categories: Seq[String], thumbnail: Option[String], language: Option[String], status: Option[String],
  goodReadsID: Option[String], goodReadsRatingsAvg: Option[Double], goodReadsRatingsCount: Option[Int])

object BookType {

  implicit val BookType = deriveObjectType[RootResolver, Book]()

  val BookMutationType = deriveContextObjectType[RootResolver, BookResolver, Unit](
    _.mutation.book,
    IncludeMethods("add"))

}

object BookTable extends SQLSyntaxSupport[Book] {
  override val tableName = "books"

  override val columns = autoColumns[Book]()

  def arrayToList[T](array: java.sql.Array): List[T] = {
    array.getArray().asInstanceOf[List[T]]
  }

  def apply(b: ResultName[Book])(rs: WrappedResultSet): Book = {
    // autoConstruct(rs, b)
    Book(
      rs.string(b.title), rs.stringOpt(b.subtitle), arrayToList[String](rs.array(b.authors)), rs.stringOpt(b.publisher),
      rs.localDateOpt(b.publishedAt), rs.stringOpt(b.description), arrayToList[String](rs.array(b.categories)),
      rs.stringOpt(b.thumbnail), rs.stringOpt(b.language), rs.stringOpt(b.status), rs.stringOpt(b.goodReadsID),
      rs.doubleOpt(b.goodReadsRatingsAvg), rs.intOpt(b.goodReadsRatingsCount))
  }
}
