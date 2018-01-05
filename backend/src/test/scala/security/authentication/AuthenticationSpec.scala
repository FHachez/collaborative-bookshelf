package security.authentication

import database.Database
import org.scalatest.{AsyncFlatSpec, BeforeAndAfterAll, Matchers}
import types.security.authentication.User

class AuthenticationSpec extends AsyncFlatSpec with BeforeAndAfterAll with Matchers {

  override def beforeAll {
    Database.init
  }

  override def afterAll {
    Database.stop
  }

  behavior of "Authentication"

  it should "authenticate successfully" in {
    AuthenticationHandler.createUser("test", 1, "test")

    AuthenticationHandler.authenticate("test", "test").map { user =>
      user should matchPattern { case Some(User("test", 1, _, _)) => }
    }
  }

}
