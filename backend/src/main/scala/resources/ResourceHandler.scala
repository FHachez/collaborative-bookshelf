package resources

import java.nio.file.{ Files, Path, Paths }

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import com.typesafe.config.ConfigFactory

object ResourceHandler {

  val dataFolder = ConfigFactory.load().getString("resources.dataFolder")

  def getEidImage(fileName: String): Future[Array[Byte]] = {
    Future {
      Files.readAllBytes(Paths.get(s"$dataFolder/$fileName"))
    }
  }

  def getEidPath(fileName: String): Future[Path] = {
    Future[Path] {
      Paths.get(s"$dataFolder/$fileName")
    }
  }

}
