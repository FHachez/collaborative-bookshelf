package graphql

import com.typesafe.scalalogging.LazyLogging
import database.Database
import org.scalatest.{AsyncFlatSpec, BeforeAndAfterAll, Matchers}
import sangria.parser.QueryParser
import spray.json.{JsObject, JsString, JsonParser}

class BookResolverSpec extends AsyncFlatSpec with Matchers with BeforeAndAfterAll with LazyLogging {

  //TODO: Test are dependent on each other due to database operations. Should become independent!

  override def beforeAll {
    Database.init
  }

  override def afterAll {
    Database.stop
  }

  behavior of "BookResolver"

  it should "return a BooleanType of true for mutation *addBook*" in {

    val jsonQuery =
      """{
        |	"query":
        |   "mutation root($title: String!, $subTitle: String, $authors: [String!]!, $publisher: String,
        |                  $publishedAt: Date, $description: String, $categories: [String!]!, $thumbnail: String,
        |                  $language: String, $status: String, $goodReadsID: String, $goodReadsRatingsAvg: Float,
        |                  $goodReadsRatingsCount: Int)
        |    {
        |       book {
        |         add(
        |           title: $title, subTitle: $subTitle, authors: $authors, publisher: $publisher,
        |           publishedAt: $publishedAt, description: $description, categories: $categories,
        |           thumbnail: $thumbnail, language: $language, status: $status, goodReadsID: $goodReadsID,
        |           goodReadsRatingsAvg: $goodReadsRatingsAvg, goodReadsRatingsCount: $goodReadsRatingsCount
        |         )
        |       }
        |   }",
        |	"variables": {
        |    "title": "Harry Potter", "subTitle": "Phoenix", "authors": ["J", "K", "Rowling"], "publishedAt": "1993-10-16",
        |    "categories": [], "goodReadsRatingsAvg": 4.7, "goodReadsRatingsCount": 4000
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
          "book": {
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

  it should "return a Book for query *getBook*" in {

    val jsonQuery =
      """{
        |	"query":
        |   "query root($id: Long!)
        |    {
        |       book(id: $id) {
        |         title,
        |         categories,
        |         publishedAt
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
          "book": {
            "title": "Harry Potter",
            "categories": [],
            "publishedAt": "1993-10-16"
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
