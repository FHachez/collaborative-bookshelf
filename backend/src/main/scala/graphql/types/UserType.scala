package graphql.types

import graphql.resolvers.{ RootResolver, UserResolver}
import sangria.macros.derive._
import scalikejdbc._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

case class User(id: Long, email: String, firstName: String, lastName: Option[String])

object UserType {

  implicit val UserType = deriveObjectType[RootResolver, User]()

  val  UserMutationType = deriveContextObjectType[RootResolver, UserResolver, Unit](
    _.mutation.user)

}

object UserTable extends SQLSyntaxSupport[User] {

  implicit val session = AutoSession

  def createTable: Unit = {
    val query =
      sql"""
         CREATE TABLE IF NOT EXISTS users (
             id SERIAL NOT NULL,
             email TEXT NOT NULL,
             first_name TEXT NOT NULL,
             last_name TEXT,
             PRIMARY KEY( id )
         );

         CREATE INDEX IF NOT EXISTS users_id_idx ON users(id);
         """

    query.execute.apply
  }

  val u = this.syntax("u")

  override val tableName = "users"

  override val columns = autoColumns[User]()

  def apply(rs: WrappedResultSet): User = {
    autoConstruct(rs, u)
  }

  def get(id: Long): Future[Option[User]] = {
    Future {
      val query =
        sql"""
           SELECT ${u.result.*}
           FROM ${UserTable as u}
           WHERE ${column.id} = $id
         """

      query.map(UserTable(_)).first.apply
    }
  }

  def exists(email: String): Boolean = {
    val query =
      sql"""
        SELECT ${u.result.*}
        FROM ${UserTable as u}
        WHERE ${column.email} = ${email}
        LIMIT 1
      """

    query.map(UserTable(_)).first.apply match {
      case Some(_) => true
      case _ => false
    }
  }

  def insert(user: User): Future[Boolean] = {

    Future {
      val query =
        sql"""
          INSERT INTO ${UserTable.table} (
            ${column.email},
            ${column.firstName},
            ${column.lastName}
          )
          VALUES (
            ${user.email},
            ${user.firstName},
            ${user.lastName}
          )
        """

      query.update.apply match {
        case 1 => true
        case _ => false
      }

    }
  }

  def update(user: User): Future[Boolean] = {

    val query =
      sql"""
      UPDATE ${UserTable.table}
      SET
        ${column.email} = ${user.email},
        ${column.firstName} = ${user.firstName},
        ${column.lastName} = ${user.lastName}
      WHERE ${column.id} = ${user.id}
      """

    Future {
      query.update.apply match {
        case 1 => true
        case _ => false
      }
    }
  }

}
