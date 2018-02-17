package graphql

import com.typesafe.scalalogging.LazyLogging
import database.Database
import org.scalatest.{AsyncFlatSpec, BeforeAndAfterAll, Matchers}
import sangria.parser.QueryParser
import spray.json.{JsObject, JsString, JsonParser}

class BookResolverSpec extends AsyncFlatSpec with Matchers with BeforeAndAfterAll with LazyLogging {

  override def beforeAll {
    Database.init
  }

  override def afterAll {
    Database.stop
  }

  behavior of "ClientResolver"

  it should "return a BooleanType of true for mutation *addClient*" in {

    val jsonQuery =
      """{
        |	"query":
        |   "mutation root($firstName: String!, $middleName: String, $lastName: String!, $gender: String!,
        |                  $dateOfBirth: Date!, $placeOfBirth: String!, $nationalNumber: String!, $nationality: String!,
        |                  $municipality: String!, $zipCode: String!, $streetName: String!, $number: String!)
        |    {
        |       client {
        |       add(
        |         firstName: $firstName, middleName: $middleName, lastName: $lastName, gender: $gender,
        |         dateOfBirth: $dateOfBirth, placeOfBirth: $placeOfBirth, nationalNumber: $nationalNumber,
        |         nationality: $nationality, municipality: $municipality, zipCode: $zipCode,
        |         streetName: $streetName, number: $number
        |       )
        |     }
        |   }",
        |	"variables": {
        |
        | },
        |	"operationName": "root"
        |}""".stripMargin.replaceAll("\n", "").replaceAll("\\s+", " ")

    val JsObject(fields) = JsonParser(jsonQuery)

    val JsString(query) = fields("query")

    val operation = fields.get("operationName") collect {
      case JsString(op) => op
    }

    val vars = fields.get("variables") match {
      case Some(obj: JsObject) => obj
      case _ => JsObject.empty
    }

    val expectedResponse = JsonParser(
      """
      {
        "data": {
          "client": {
            "add": true
          }
        }
      }
      """.stripMargin
    )

    GraphqlExecutor.execute(QueryParser.parse(query).get, operation, vars, "", 1).map { response =>
      response should be (expectedResponse)
    }
  }
}
