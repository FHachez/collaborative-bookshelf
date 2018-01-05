package rest.routes

import akka.actor.ActorSystem
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes.InternalServerError
import akka.http.scaladsl.server.{ Directives, Route }
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.FileIO
import resources.ResourceHandler

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Failure, Success }

class UploadRoutes(companyID: Long)(implicit system: ActorSystem, materializer: ActorMaterializer) extends Directives {

  val uploadRoutes: Route =
    pathPrefix("upload") {

      fileUpload("eid") {
        case (metadata, byteSource) =>

          onComplete(
            ResourceHandler.getEidPath(companyID, metadata.fileName).map { path =>
              byteSource
                .runWith(
                  FileIO.toPath(path))
            }) {

              case Success(_) => complete("ok")
              case Failure(e) =>
                e.printStackTrace()
                complete(HttpResponse(InternalServerError))
            }

      }
    }
}
