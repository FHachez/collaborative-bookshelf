package security.types

import anorm._
import anorm.SqlParser.{ get => parse }

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import database.Database.conn

case class Account(username: String, password: String, salt: String)

object AccountTable {

  def parser(): RowParser[Account] = {

    for {
      username <- parse[String]("username")
      password <- parse[String]("password")
      salt <- parse[String]("salt")
    } yield Account(username, password, salt)

  }

  def get(username: String): Future[Option[Account]] = {

    val query =
      SQL"""
         SELECT *
         FROM accounts
         WHERE username = $username
       """

    Future {
      query.as(parser().*).headOption
    }
  }

  def insert(account: Account): Future[Boolean] = {
    val query =
      SQL"""
        INSERT INTO accounts (
          username,
          password,
          salt
        )
        VALUES (
          ${account.username},
          ${account.password},
          ${account.salt}
        )
      """

    Future {
      query.executeUpdate() match {
        case 1 => true
        case _ => false
      }
    }
  }

}
