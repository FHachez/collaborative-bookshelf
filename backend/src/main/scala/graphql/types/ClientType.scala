package graphql.types

import graphql.resolvers.{ ClientResolver, RootResolver }
import sangria.macros.derive._
import sangria.schema.ObjectType

object ClientType {

  implicit val dateType = CustomTypes.DateType

  val ClientType: ObjectType[RootResolver, Unit] =
    deriveContextObjectType[RootResolver, ClientResolver, Unit](
      _.client,
      IncludeMethods("add"))

}
