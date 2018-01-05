package events.clients

import java.time.LocalDate

case class EditClientEvent(clientID: Long, firstName: Option[String], middleName: Option[String], lastName: Option[String],
                      gender: Option[String], dateOfBirth: Option[LocalDate],
                      placeOfBirth: Option[String], nationalNumber: Option[String], nationality: Option[String],
                      municipality: Option[String], zipCode: Option[String], streetName: Option[String],
                      number: Option[String])
