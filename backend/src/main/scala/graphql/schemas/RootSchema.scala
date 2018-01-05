package graphql.schemas

import graphql.resolvers.RootResolver
import graphql.types.ClientType._
import sangria.schema.{ Field, ObjectType, Schema, fields }

object RootSchema {

  val QueryType = ObjectType("root", fields[RootResolver, Unit](
    Field("client", ClientType, resolve = _ => ())))

  val MutationType = ObjectType("root", fields[RootResolver, Unit](
    Field("client", ClientType, resolve = _ => ())))

  val PtSchema = Schema(QueryType, Some(MutationType))

}
