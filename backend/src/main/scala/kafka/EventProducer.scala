package kafka

import java.util.Properties

import com.typesafe.scalalogging.LazyLogging
import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}

import scala.concurrent.Future
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

object EventProducer extends LazyLogging {

  val props = new Properties()
  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092")
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer])
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer])
  props.put("schema.registry.url", "http://localhost:8081")

  val producer = new KafkaProducer[Any, Any](props)

  def send(topic: String, key: Option[GenericRecord], event: GenericRecord): Future[Boolean] = {

    val producerRecord = key match {
      case Some(keyValue) => new ProducerRecord[Any, Any](topic, keyValue, event)
      case None => new ProducerRecord[Any, Any](topic, event)
    }

    Future {
      producer.send(producerRecord).get
    }.transform {
      case Success(_) => Success(true)
      case Failure(error) => Failure(error)
    }
  }
}
