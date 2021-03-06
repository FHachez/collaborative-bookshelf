package graphql

import com.typesafe.scalalogging.LazyLogging
import graphql.resolvers.RootResolver
import graphql.schemas.RootSchema.CbSchema
import sangria.ast.Document
import sangria.execution.Executor
import spray.json.{ JsObject, JsValue }
import sangria.marshalling.sprayJson._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object GraphqlExecutor extends LazyLogging {

  def execute(queryAst: Document, operation: Option[String], vars: JsObject, username: String): Future[JsValue] = {

    Executor.execute(
      CbSchema,
      queryAst,
      userContext = new RootResolver(username),
      variables = vars,
      operationName = operation)
  }
}
