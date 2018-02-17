package security.authentication

import com.softwaremill.session._
import org.json4s.JValue

case class SessionData(username: String, expiryDate: String, role: String)

object SessionHandler {

  implicit val sessionSerializer: SessionSerializer[SessionData, JValue] = JValueSessionSerializer.caseClass[SessionData]
  implicit val sessionEncoder: JwtSessionEncoder[SessionData] = new JwtSessionEncoder[SessionData]
  implicit val sessionManager: SessionManager[SessionData] = new SessionManager(SessionConfig.fromConfig())

}
