package graphql.schemas

import graphql.resolvers.{ QueryResolver, RootResolver }
import graphql.types.BookType._
import sangria.schema.{ Field, ObjectType, Schema, fields }
import sangria.macros.derive._

object RootSchema {

  val QueryType = deriveContextObjectType[RootResolver, QueryResolver, Unit](
    _.query,
    ObjectTypeName("query"),
    IncludeMethods("book"))

  //  val QueryType = ObjectType("root", fields[RootResolver, Unit](
  //    Field("book", BookType,
  //      description = Some("Returns a product with specific `id`."),
  //      arguments = Id :: Nil,
  //      resolve = c ⇒ c.ctx.product(c arg Id)))))

  val MutationType = ObjectType("mutate", fields[RootResolver, Unit](
    Field("book", BookMutationType, resolve = _ => ())))

  val CbSchema = Schema(QueryType, Some(MutationType))

}
