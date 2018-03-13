package graphql.types

import java.time.LocalDate

import graphql.resolvers.{BookResolver, RootResolver}
import CustomTypes._
import sangria.macros.derive.{GraphQLField, _}
import anorm._
import anorm.SqlParser.{ get => parse }

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import database.Database.conn

case class Book(id: Long, title: String, subTitle: Option[String], authors: Seq[String],
           publisher: Option[String], publishedAt: Option[LocalDate], description: Option[String],
           categories: Seq[String], thumbnail: Option[String], language: Option[String],
           status: Option[String], goodReadsID: Option[String], goodReadsRatingsAvg: Option[Double],
           goodReadsRatingsCount: Option[Int]) {


  @GraphQLField
  def readBy(): String = {
    println("Calling *readBy* function")
    return "test"
  }

}

object BookType {

  implicit val BookType = deriveObjectType[RootResolver, Book]()

  val BookMutationType = deriveContextObjectType[RootResolver, BookResolver, Unit](
    _.mutation.book)

}

object BookTable {

  def createTable: Unit = {
    val query =
      SQL"""
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

    query.executeInsert()
  }

  def parser(): RowParser[Book] = {

    for {
      id <- parse[Long]("id")
      title <- parse[String]("title")
      subTitle <- parse[Option[String]]("sub_title")
      authors <- parse[List[String]]("authors")
      publisher <- parse[Option[String]]("publisher")
      publishedAt <- parse[Option[LocalDate]]("published_at")
      description <- parse[Option[String]]("description")
      categories <- parse[List[String]]("categories")
      thumbnail <- parse[Option[String]]("thumbnail")
      language <- parse[Option[String]]("language")
      status <- parse[Option[String]]("status")
      goodReadsID <- parse[Option[String]]("good_reads_id")
      goodReadsRatingsAvg <- parse[Option[Double]]("good_reads_ratings_avg")
      goodReadsRatingsCount <- parse[Option[Int]]("good_reads_ratings_count")
    } yield Book(
      id, title, subTitle, authors, publisher, publishedAt, description, categories, thumbnail,
      language, status, goodReadsID, goodReadsRatingsAvg, goodReadsRatingsCount)

  }

  def get(id: Long): Future[Option[Book]] = {
    Future {
      val query =
        SQL"""
           SELECT *
           FROM books
           WHERE id = $id
         """

      query.as(parser().*).headOption
    }
  }

  def exists(title: String): Boolean = {
    val query =
      SQL"""
        SELECT 1
        FROM books
        WHERE title = ${title}
        LIMIT 1
      """

    query.executeQuery.resultSet.apply(_.isBeforeFirst())
  }

  def insert(book: Book): Future[Boolean] = {

    val query =
      SQL"""
      INSERT INTO books (
        title,
        sub_title,
        authors,
        publisher,
        published_at,
        description,
        categories,
        thumbnail,
        language,
        status,
        good_reads_id,
        good_reads_ratings_avg,
        good_reads_ratings_count
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
      query.executeUpdate() match {
        case 1 => true
        case _ => false
      }
    }
  }

  def update(book: Book): Future[Boolean] = {

    val query =
      SQL"""
      UPDATE books
      SET
        title = ${book.title},
        sub_title = ${book.subTitle},
        authors = ARRAY[${book.authors}]::text[],
        publisher = ${book.publisher},
        published_at = ${book.publishedAt},
        description = ${book.description},
        categories = ARRAY[${book.categories}]::text[],
        thumbnail = ${book.thumbnail},
        language = ${book.language},
        status = ${book.status},
        good_reads_id = ${book.goodReadsID},
        good_reads_ratings_avg = ${book.goodReadsRatingsAvg},
        good_reads_ratings_count = ${book.goodReadsRatingsCount}
      WHERE id = ${book.id}
      """

    Future {
      query.executeUpdate() match {
        case 1 => true
        case _ => false
      }
    }
  }

}
