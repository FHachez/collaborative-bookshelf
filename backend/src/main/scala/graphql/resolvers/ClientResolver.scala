package graphql.resolvers

import java.time.LocalDate

import com.sksamuel.avro4s.RecordFormat
import events.clients.{AddClientEvent, EditClientEvent}
import kafka.{AvroFactory, EventProducer}
import sangria.macros.derive._

import scala.concurrent.Future

class ClientResolver(companyID: Long) {

  @GraphQLField
  def add(firstName: String, middleName: Option[String], lastName: String, gender: String, dateOfBirth: LocalDate,
          placeOfBirth: Option[String], nationalNumber: Option[String], nationality: Option[String],
          municipality: Option[String], zipCode: Option[String], streetName: Option[String],
          number: Option[String]): Future[Boolean] = {

    val avroFactory = new AvroFactory[AddClientEvent] {
      val eventFormat = RecordFormat[AddClientEvent]
    }

    val key = avroFactory.createKey(companyID, None)

    val event = avroFactory.createEvent(
      AddClientEvent(
        firstName, middleName, lastName, gender, dateOfBirth,
        placeOfBirth, nationalNumber, nationality, municipality,
        zipCode, streetName, number
      ))

    EventProducer.send("add-client-events", Some(key), event)

  }

  @GraphQLField
  def edit(clientID: Long, firstName: Option[String], middleName: Option[String], lastName: Option[String],
           gender: Option[String], dateOfBirth: Option[LocalDate],
          placeOfBirth: Option[String], nationalNumber: Option[String], nationality: Option[String],
          municipality: Option[String], zipCode: Option[String], streetName: Option[String],
          number: Option[String]): Future[Boolean] = {

    val avroFactory = new AvroFactory[EditClientEvent] {
      val eventFormat = RecordFormat[EditClientEvent]
    }

    val key = avroFactory.createKey(companyID, Some(clientID.toString))

    val event = avroFactory.createEvent(
      EditClientEvent(
        clientID, firstName, middleName, lastName, gender, dateOfBirth,
        placeOfBirth, nationalNumber, nationality, municipality,
        zipCode, streetName, number
      ))

    EventProducer.send("edit-client-events", Some(key), event)

  }
}