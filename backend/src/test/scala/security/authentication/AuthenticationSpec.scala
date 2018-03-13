package security.authentication

import database.Database
import org.scalatest.{AsyncFlatSpec, BeforeAndAfterAll, Matchers}

import scala.concurrent.Future

class AuthenticationSpec extends AsyncFlatSpec with BeforeAndAfterAll with Matchers {

  override def beforeAll {
    Database.init
  }

  override def afterAll {
    Database.stop
  }

  behavior of "Authentication"

  it should "authenticate successfully" in {
    Future {
      assert(true)
    }
  }

}
