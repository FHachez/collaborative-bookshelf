package graphql.resolvers

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.LazyLogging
import graphql.types.{User, UserTable}
import sangria.macros.derive._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UserResolver() extends LazyLogging {

  def get(id: Long): Future[Option[User]] = {
    UserTable.get(id)
  }

  @GraphQLField
  def add(email: String, firstName: String, lastName: Option[String]): Future[Boolean] = {

    val companyEmail = ConfigFactory.load().getString("company.valid-email")

    if(!email.contains(companyEmail)) {
      return Future { false }
    }

    if(UserTable.exists(email)) {
      return Future { false }
    }

    val user = User(-1, email, firstName, lastName)

    UserTable.insert(user)

  }

  @GraphQLField
  def update(id: Long, email: String, firstName: String, lastName: Option[String]): Future[Boolean] = {

    if(!UserTable.exists(email)) {
      return Future { false }
    }

    val user = User(
      id, email, firstName, lastName)

    UserTable.update(user)

  }

}