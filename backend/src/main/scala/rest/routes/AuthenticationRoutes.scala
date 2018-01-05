package rest.routes

import akka.http.scaladsl.server.{ Directives, Route }
import spray.json._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes.Forbidden
import akka.http.scaladsl.model.{ HttpEntity, HttpResponse }
import com.softwaremill.session.SessionDirectives._
import com.softwaremill.session.SessionOptions._
import com.typesafe.scalalogging.LazyLogging
import rest.schemas.Login.LoginForm
import security.authentication.{ AuthenticationHandler, SessionData }
import security.authentication.SessionHandler._

import scala.util.{ Failure, Success }

/*

  ######################################
  #                                    #
  #           JSON OBJECTS             #
  #                                    #
  ######################################

  */

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val loginForm = jsonFormat2(LoginForm)
}

trait AuthenticationRoutes extends Directives with JsonSupport with LazyLogging {

  /*

  ######################################
  #                                    #
  #             SECURITY               #
  #                                    #
  ######################################

  */

  val authenticationRoutes: Route =
    pathPrefix("auth") {

      path("login") {
        post {
          entity(as[LoginForm]) { body =>
            onComplete(AuthenticationHandler.authenticate(body.username, body.password)) {
              case Success(Some(user)) =>
                setSession(oneOff, usingHeaders, SessionData(user.username, user.companyID, user.expiryDate, user.role)) { ctx =>
                  ctx.complete("ok")
                }

              case Failure(e) =>
                e.printStackTrace()
                complete(HttpResponse(Forbidden, entity = HttpEntity("Invalid credentials")))
            }
          }
        }
      } ~
        path("logout") {
          get {
            invalidateSession(oneOff, usingHeaders) {
              complete("logged out")
            }
          }
        }
    }
}
