package graphql.types

import graphql.resolvers.{ RootResolver, UserResolver}
import sangria.macros.derive._

import anorm._
import anorm.SqlParser.{ get => parse }

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import database.Database.conn

case class User(id: Long, email: String, firstName: String, lastName: Option[String])

object UserType {

  implicit val UserType = deriveObjectType[RootResolver, User]()

  val  UserMutationType = deriveContextObjectType[RootResolver, UserResolver, Unit](
    _.mutation.user)

}

object UserTable {

  def createTable: Unit = {
    val query =
      SQL"""
       CREATE TABLE IF NOT EXISTS users (
           id SERIAL NOT NULL,
           email TEXT NOT NULL,
           first_name TEXT NOT NULL,
           last_name TEXT,
           PRIMARY KEY( id )
       );

       CREATE INDEX IF NOT EXISTS users_id_idx ON users(id);
       """

    query.executeInsert()
  }

  def parser(): RowParser[User] = {

    for {
      id <- parse[Long]("id")
      email <- parse[String]("email")
      firstName <- parse[String]("first_name")
      lastName <- parse[Option[String]]("last_name")
    } yield User(id, email, firstName, lastName)

  }

  def get(id: Long): Future[Option[User]] = {
    Future {
      val query =
        SQL"""
           SELECT *
           FROM users
           WHERE id = $id
         """

      query.as(parser().*).headOption
    }
  }

  def exists(email: String): Boolean = {
    val query =
      SQL"""
        SELECT 1
        FROM users
        WHERE email = ${email}
        LIMIT 1
      """

    query.executeQuery.resultSet.apply(_.isBeforeFirst())
  }

  def insert(user: User): Future[Boolean] = {

    val query =
      SQL"""
        INSERT INTO users (
          email,
          first_name,
          last_name
        )
        VALUES (
          ${user.email},
          ${user.firstName},
          ${user.lastName}
        )
      """

      Future {
        query.executeUpdate() match {
          case 1 => true
          case _ => false
        }
      }
  }

  def update(user: User): Future[Boolean] = {

    val query =
      SQL"""
      UPDATE users
      SET
        email = ${user.email},
        first_name = ${user.firstName},
        last_name = ${user.lastName}
      WHERE id = ${user.id}
      """

    Future {
      query.executeUpdate() match {
        case 1 => true
        case _ => false
      }
    }
  }

}
