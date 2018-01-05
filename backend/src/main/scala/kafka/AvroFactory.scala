package kafka

import java.time.{LocalDate, ZoneId}

import com.sksamuel.avro4s.{FromValue, RecordFormat}
import org.apache.avro.Schema.Field
import org.apache.avro.generic.GenericRecord

trait AvroFactory[T] {

  case class EventKey(companyID: Long, topic: Option[String])

  implicit object LocalDateFromValue extends FromValue[LocalDate] {
    override def apply(value: Any, field: Field): Long = {

      val zoneId = ZoneId.systemDefault()
      LocalDate.parse(value.toString).atStartOfDay(zoneId).toEpochSecond
    }
  }

  val eventFormat: RecordFormat[T]
  val keyFormat: RecordFormat[EventKey] = RecordFormat[EventKey]

  def createEvent(event: T): GenericRecord = {
    eventFormat.to(event)
  }

  def createKey(companyID: Long, eventKey: Option[String]): GenericRecord = {
    keyFormat.to(EventKey(companyID, eventKey))
  }

}
