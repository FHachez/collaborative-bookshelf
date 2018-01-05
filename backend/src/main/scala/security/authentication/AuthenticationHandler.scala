package security.authentication

import com.github.t3hnar.bcrypt._
import security.authentication.Roles.ROLE._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import java.time.LocalDateTime

import scalikejdbc._
import com.typesafe.scalalogging.LazyLogging
import types.security.authentication.{ Account, User }
import types.security.authentication.Account.a

object AuthenticationHandler extends LazyLogging {

  val daysUntilExpiry = 7

  def authenticate(username: String, passwordToCheck: String): Future[Option[User]] = {

    Future {
      val query =
        sql"""
           SELECT ${a.result.*}
           FROM ${Account as a}
           WHERE username = $username
         """

      logger.info(query.toString)

      val result: Option[Account] = DB.readOnly(implicit session =>
        query.map(Account(_)).first.apply)

      result match {
        case Some(Account(name, companyID, password, salt)) =>
          val hashedPassword = passwordToCheck.bcrypt(salt)

          if (hashedPassword == password) {
            val expiryDate = LocalDateTime.now().plusDays(daysUntilExpiry).toString

            Some(User(name, companyID, expiryDate, BASIC_ROLE))
          } else {
            None
          }

        case None => None

      }
    }
  }

  def createUser(username: String, companyID: Long, password: String): Future[_] = {

    Future {
      val salt = generateSalt
      val hashedPassword = password.bcrypt(salt)

      val query =
        sql"""
        INSERT INTO accounts (username, company_id, password, salt)
        VALUES ($username, $companyID, $hashedPassword, $salt)
        """

      DB localTx { implicit session =>
        query.update.apply
      }
    }
  }

  def isExpired(expiryDate: String): Boolean = {
    val now = LocalDateTime.now()
    val toCheck = LocalDateTime.parse(expiryDate)

    now.isAfter(toCheck)
  }
}
