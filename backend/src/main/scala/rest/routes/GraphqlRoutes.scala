package rest.routes

import akka.http.scaladsl.model.{ HttpEntity, HttpResponse }
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.{ Route, ValidationRejection }
import akka.http.scaladsl.server.directives.PathDirectives.path
import graphql.GraphqlExecutor
import rest.WebServer.reject
import sangria.execution.{ ErrorWithResolver, QueryAnalysisError }
import sangria.parser.QueryParser
import spray.json.{ JsObject, JsString, JsValue }

import scala.util.{ Failure, Success }

class GraphqlRoutes(username: String) extends JsonSupport {

  val graphqlRoutes: Route =
    path("graphql") {
      post {

        entity(as[JsValue]) { requestJson =>

          val JsObject(fields) = requestJson

          val JsString(query) = fields("query")

          val operation = fields.get("operationName") collect {
            case JsString(op) => op
          }

          val vars = fields.get("variables") match {
            case Some(obj: JsObject) => obj
            case _ => JsObject.empty
          }

          QueryParser.parse(query) match {

            // query parsed successfully, time to execute it!
            case Success(queryAst) =>
              onComplete(GraphqlExecutor.execute(queryAst, operation, vars, username)) {

                case Success(responseJson) =>
                  complete(responseJson.prettyPrint)

                case Failure(error: QueryAnalysisError) =>
                  complete(HttpResponse(BadRequest, entity = HttpEntity(error.getMessage)))

                case Failure(error: ErrorWithResolver) =>
                  error.printStackTrace()
                  complete(HttpResponse(InternalServerError))
              }

            // can't parse GraphQL query, return error
            case Failure(error) =>
              complete(HttpResponse(BadRequest, entity = HttpEntity(error.getMessage)))

          }
        }
      }
    }

}
