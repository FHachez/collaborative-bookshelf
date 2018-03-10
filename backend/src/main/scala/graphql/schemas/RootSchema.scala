package graphql.schemas

import graphql.resolvers.{ QueryResolver, RootResolver }
import graphql.types.BookType._
import graphql.types.UserType._
import sangria.schema.{ Field, ObjectType, Schema, fields }
import sangria.macros.derive._

object RootSchema {

  val QueryType = deriveContextObjectType[RootResolver, QueryResolver, Unit](
    _.query,
    ObjectTypeName("root"),
    IncludeMethods("book", "user"))

  val MutationType = ObjectType("root", fields[RootResolver, Unit](
    Field("book", BookMutationType, resolve = _ => ()),
    Field("user", UserMutationType, resolve = _ => ())))

  val CbSchema = Schema(QueryType, Some(MutationType))

}
