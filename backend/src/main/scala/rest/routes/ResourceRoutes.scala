package rest.routes

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{ HttpEntity, _ }
import akka.http.scaladsl.server.{ Directives, Route }
import resources.ResourceHandler

import scala.util.{ Failure, Success }

class ResourceRoutes() extends Directives {

  val resourceRoutes: Route =
    pathPrefix("resources") {

      pathPrefix("eid") {
        path("""(.+)\.jpg""".r) { matched =>
          get {
            onComplete(ResourceHandler.getEidImage(s"$matched.jpg")) {
              case Success(image) =>
                complete(HttpResponse(entity = HttpEntity(MediaTypes.`image/jpeg`, image)))
              case Failure(e) =>
                e.printStackTrace()
                complete(HttpResponse(NotFound))
            }
          }
        }
      }
    }
}
