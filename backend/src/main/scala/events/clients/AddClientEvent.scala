package events.clients

import java.time.LocalDate

case class AddClientEvent(firstName: String, middleName: Option[String], lastName: String, gender: String, dateOfBirth: LocalDate,
                          placeOfBirth: Option[String], nationalNumber: Option[String], nationality: Option[String],
                          municipality: Option[String], zipCode: Option[String], streetName: Option[String],
                          number: Option[String])
