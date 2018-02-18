package graphql.types

import java.time.LocalDate

import graphql.resolvers.{BookResolver, RootResolver}
import CustomTypes._
import sangria.macros.derive._
import scalikejdbc._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

case class Book(id: Long, title: String, subTitle: Option[String], authors: Seq[String],
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

  implicit val session = AutoSession

  def createTable: Unit = {
    val query =
      sql"""
         CREATE TABLE IF NOT EXISTS books (
             id SERIAL NOT NULL,
             title TEXT NOT NULL,
             sub_title TEXT,
             authors TEXT ARRAY NOT NULL,
             publisher TEXT,
             published_at DATE,
             description TEXT ,
             categories TEXT ARRAY NOT NULL,
             thumbnail TEXT,
             language TEXT,
             status TEXT,
             good_reads_id TEXT,
             good_reads_ratings_avg DOUBLE PRECISION,
             good_reads_ratings_count INTEGER,
             PRIMARY KEY( id )
         );

         CREATE INDEX IF NOT EXISTS books_id_idx ON books(id);
         """

    query.execute.apply
  }

  val b = this.syntax("b")

  override val tableName = "books"

  override val columns = autoColumns[Book]()

  def arrayToList[T](array: java.sql.Array): List[T] = {
    array.getArray().asInstanceOf[Array[T]].toList
  }

  def apply(rs: WrappedResultSet): Book = {
    // autoConstruct(rs, b)
    val bookColumns = b.resultName

    Book(
      rs.long(bookColumns.id), rs.string(bookColumns.title), rs.stringOpt(bookColumns.subTitle),
      arrayToList[String](rs.array(bookColumns.authors)), rs.stringOpt(bookColumns.publisher),
      rs.localDateOpt(bookColumns.publishedAt), rs.stringOpt(bookColumns.description),
      arrayToList[String](rs.array(bookColumns.categories)), rs.stringOpt(bookColumns.thumbnail),
      rs.stringOpt(bookColumns.language), rs.stringOpt(bookColumns.status), rs.stringOpt(bookColumns.goodReadsID),
      rs.doubleOpt(bookColumns.goodReadsRatingsAvg), rs.intOpt(bookColumns.goodReadsRatingsCount))
  }

  def get(id: Long): Future[Option[Book]] = {
    Future {
      val query =
        sql"""
           SELECT ${b.result.*}
           FROM ${BookTable as b}
           WHERE id = $id
         """

      query.map(BookTable(_)).first.apply
    }
  }

  def insert(book: Book): Future[Boolean] = {

//    Future {
//      val query =
//        withSQL {
//          insert.into(BookTable).values(
//            book.productIterator.toList: _*)
//        }
//
//      query.update.apply match {
//        case 1 => true
//        case _ => false
//      }
//
//    }

    val query =
      sql"""
      INSERT INTO ${BookTable.table} (
        ${column.title},
        ${column.subTitle},
        ${column.authors},
        ${column.publisher},
        ${column.publishedAt},
        ${column.description},
        ${column.categories},
        ${column.thumbnail},
        ${column.language},
        ${column.status},
        ${column.goodReadsID},
        ${column.goodReadsRatingsAvg},
        ${column.goodReadsRatingsCount}
      )
      VALUES (
        ${book.title},
        ${book.subTitle},
        ARRAY[${book.authors}]::text[],
        ${book.publisher},
        ${book.publishedAt},
        ${book.description},
        ARRAY[${book.categories}]::text[],
        ${book.thumbnail},
        ${book.language},
        ${book.status},
        ${book.goodReadsID},
        ${book.goodReadsRatingsAvg},
        ${book.goodReadsRatingsCount}
      )
      """
    Future {
      query.update.apply match {
        case 1 => true
        case _ => false
      }
    }
  }
}
