package security.authentication

import com.github.t3hnar.bcrypt._
import security.authentication.Roles.ROLE._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import java.time.LocalDateTime

import com.typesafe.scalalogging.LazyLogging
import security.types.{ Account, User, AccountTable }

object AuthenticationHandler extends LazyLogging {

  val daysUntilExpiry = 7

  def authenticate(username: String, passwordToCheck: String): Future[Option[User]] = {

      AccountTable.get(username).flatMap {
        case Some(Account(name, password, salt)) =>
          val hashedPassword = passwordToCheck.bcrypt(salt)

          if (hashedPassword == password) {
            val expiryDate = LocalDateTime.now().plusDays(daysUntilExpiry).toString

            Future { Some(User(name, expiryDate, BASIC_ROLE)) }
          } else {
            Future { None }
          }

        case None => Future { None }
    }
  }

  def createUser(username: String, password: String): Future[Boolean] = {

    val salt = generateSalt
    val hashedPassword = password.bcrypt(salt)

    val account = Account(username, hashedPassword, salt)

    AccountTable.insert(account)

  }

  def isExpired(expiryDate: String): Boolean = {
    val now = LocalDateTime.now()
    val toCheck = LocalDateTime.parse(expiryDate)

    now.isAfter(toCheck)
  }
}
