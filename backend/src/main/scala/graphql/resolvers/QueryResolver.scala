package graphql.resolvers

import com.typesafe.scalalogging.LazyLogging
import graphql.types.{Book, User}
import sangria.macros.derive._

import scala.concurrent.Future

class QueryResolver(username: String) extends LazyLogging {

  @GraphQLField
  def book(id: Long): Future[Option[Book]] = new BookResolver().get(id)

  @GraphQLField
  def user(id: Long): Future[Option[User]] = new UserResolver().get(id)

}