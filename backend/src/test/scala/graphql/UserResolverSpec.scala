package graphql

import com.typesafe.scalalogging.LazyLogging
import database.Database
import org.scalatest.{AsyncFlatSpec, BeforeAndAfterAll, Matchers}
import sangria.parser.QueryParser
import spray.json.{JsObject, JsString, JsonParser}

class UserResolverSpec extends AsyncFlatSpec with Matchers with BeforeAndAfterAll with LazyLogging {

  //TODO: Test are dependent on each other due to database operations. Should become independent!

  override def beforeAll {
    Database.init
  }

  override def afterAll {
    Database.stop
  }

  behavior of "UserResolver"

  it should "return a BooleanType of true for mutation *addUser*" in {

    val jsonQuery =
      """{
        |	"query":
        |   "mutation root($email: String!, $firstName: String!, $lastName: String)
        |    {
        |       user {
        |         add(
        |           email: $email, firstName: $firstName, lastName: $lastName
        |         )
        |       }
        |   }",
        |	"variables": {
        |    "email": "milanvdm@riaktr.com", "firstName": "Milan", "lastName": "van der Meer"
        | }
        |}""".stripMargin.replaceAll("\n", "").replaceAll("\\s+", " ")

    val JsObject(fields) = JsonParser(jsonQuery)

    val JsString(query) = fields("query")

    val vars = fields.get("variables") match {
      case Some(obj: JsObject) => obj
      case _ => JsObject.empty
    }

    val expectedResponse = JsonParser(
      """
      {
        "data": {
          "user": {
            "add": true
          }
        }
      }
      """.stripMargin
    )

    GraphqlExecutor.execute(QueryParser.parse(query).get, Some("root"), vars, "").map { response =>
      response should be (expectedResponse)
    }
  }

  it should "return a User for query *getUser*" in {

    val jsonQuery =
      """{
        |	"query":
        |   "query root($id: Long!)
        |    {
        |       user(id: $id) {
        |         email,
        |         firstName,
        |         lastName
        |       }
        |   }",
        |	"variables": {
        |    "id": 1
        | }
        |}""".stripMargin.replaceAll("\n", "").replaceAll("\\s+", " ")

    val JsObject(fields) = JsonParser(jsonQuery)

    val JsString(query) = fields("query")

    val vars = fields.get("variables") match {
      case Some(obj: JsObject) => obj
      case _ => JsObject.empty
    }

    val expectedResponse = JsonParser(
      """
      {
        "data": {
          "user": {
            "email": "milanvdm@riaktr.com",
            "firstName": "Milan",
            "lastName": "van der Meer"
          }
        }
      }
      """.stripMargin
    )

    GraphqlExecutor.execute(QueryParser.parse(query).get, Some("root"), vars, "").map { response =>
      response should be (expectedResponse)
    }
  }

  it should "fail as the User already exists for query *addUser*" in {

    val jsonQuery =
      """{
        |	"query":
        |   "mutation root($email: String!, $firstName: String!, $lastName: String)
        |    {
        |       user {
        |         add(
        |           email: $email, firstName: $firstName, lastName: $lastName
        |         )
        |       }
        |   }",
        |	"variables": {
        |    "email": "milanvdm@riaktr.com", "firstName": "Milan", "lastName": "van der Meer"
        | }
        |}""".stripMargin.replaceAll("\n", "").replaceAll("\\s+", " ")

    val JsObject(fields) = JsonParser(jsonQuery)

    val JsString(query) = fields("query")

    val vars = fields.get("variables") match {
      case Some(obj: JsObject) => obj
      case _ => JsObject.empty
    }

    val expectedResponse = JsonParser(
      """
      {
        "data": {
          "user": {
            "add": false
          }
        }
      }
      """.stripMargin
    )

    GraphqlExecutor.execute(QueryParser.parse(query).get, Some("root"), vars, "").map { response =>
      response should be (expectedResponse)
    }
  }

  it should "update the User for query *updateUser*" in {

    val jsonQuery =
      """{
        |	"query":
        |   "mutation root($id: Long!, $email: String!, $firstName: String!, $lastName: String)
        |    {
        |       user {
        |         update(
        |           id: $id, email: $email, firstName: $firstName, lastName: $lastName
        |         )
        |       }
        |   }",
        |	"variables": {
        |    "id": 1, "email": "milanvdm@riaktr.com", "firstName": "Milan", "lastName": "van der Meer"
        | }
        |}""".stripMargin.replaceAll("\n", "").replaceAll("\\s+", " ")

    val JsObject(fields) = JsonParser(jsonQuery)

    val JsString(query) = fields("query")

    val vars = fields.get("variables") match {
      case Some(obj: JsObject) => obj
      case _ => JsObject.empty
    }

    val expectedResponse = JsonParser(
      """
      {
        "data": {
          "user": {
            "update": true
          }
        }
      }
      """.stripMargin
    )

    GraphqlExecutor.execute(QueryParser.parse(query).get, Some("root"), vars, "").map { response =>
      response should be (expectedResponse)
    }
  }
}
