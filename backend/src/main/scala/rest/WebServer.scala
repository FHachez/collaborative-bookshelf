package rest

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes.Forbidden
import akka.http.scaladsl.model.{ HttpResponse, StatusCodes }
import akka.http.scaladsl.model.headers.{ HttpOrigin, HttpOriginRange }
import akka.http.scaladsl.server._
import akka.stream.ActorMaterializer
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.{ cors, corsRejectionHandler }
import ch.megard.akka.http.cors.scaladsl.settings.CorsSettings
import com.softwaremill.session.SessionDirectives.requiredSession
import com.softwaremill.session.SessionOptions.{ oneOff, usingHeaders }
import database.Database
import rest.routes._
import security.authentication.AuthenticationHandler
import security.authentication.SessionHandler._

import scala.io.StdIn

object WebServer extends Directives with BaseRoutes with AuthenticationRoutes {

  /*

    ######################################
    #                                    #
    #           SERVER SETUP             #
    #                                    #
    ######################################

    */

  Database.init

  implicit val system = ActorSystem("be-system")
  implicit val materializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = system.dispatcher

  def main(args: Array[String]) {

    val bindingFuture = Http().bindAndHandle(routes, "localhost", 8081)

    println(s"Server online at http://localhost:8081/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }

  /*

  ######################################
  #                                    #
  #              ROUTES                #
  #                                    #
  ######################################

  */

  val corsSettings = CorsSettings.defaultSettings.copy(
    allowGenericHttpRequests = true,
    allowedOrigins = HttpOriginRange(
      HttpOrigin("http://localhost:3000")),
    exposedHeaders = List("set-authorization"))

  val rejectionHandler = corsRejectionHandler withFallback RejectionHandler.default
  val exceptionHandler = ExceptionHandler {
    case e: NoSuchElementException => complete(StatusCodes.NotFound -> e.getMessage)
  }

  val handleCorsErrors = handleRejections(rejectionHandler) & handleExceptions(exceptionHandler)

  val routes: Route =
    handleCorsErrors {
      cors(corsSettings) {
        handleCorsErrors {
          baseRoutes ~
            authenticationRoutes ~
            // Authorization of all the other endpoints
            requiredSession(oneOff, usingHeaders) { session =>
              if (AuthenticationHandler.isExpired(session.expiryDate)) {

                complete(HttpResponse(Forbidden))

              } else {

                new GraphqlRoutes(session.username, session.companyID).graphqlRoutes ~
                  new ResourceRoutes(session.companyID).resourceRoutes ~
                  new UploadRoutes(session.companyID).uploadRoutes
              }
            }
        }
      }
    }
}
