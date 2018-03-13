package rest.routes

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.PathDirectives.pathEndOrSingleSlash
import akka.http.scaladsl.server.directives.RouteDirectives.complete

trait BaseRoutes {

  // This route is the one that listens to the top level '/'
  val baseRoutes: Route =
    pathEndOrSingleSlash { // Listens to the top `/`
      complete("Server up and running") // Completes with some text
    }
}
