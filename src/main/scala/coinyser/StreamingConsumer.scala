package coinyser

import coinyser.StreamingProducerApp
import org.apache.kafka.clients.consumer.KafkaConsumer
import cats.effect.{ExitCode, IO, IOApp}

import scala.collection.JavaConversions._
import java.util


import scala.concurrent.Future


object StreamingConsumer  {


  val props = Map(
    "bootstrap.servers" -> "localhost:9092",
    "key.deserializer" ->
      "org.apache.kafka.common.serialization.IntegerDeserializer",
    "value.deserializer" ->
      "org.apache.kafka.common.serialization.StringDeserializer",
      "metadata.max.age.ms" -> "3000",
    "session.timeout.ms" -> "3000",
      "enable_auto_commit" -> "true",
  "fetch_max_wait_ms" -> "500",
  "fetch_min_bytes" -> "1000"
  )
   val records:Map[String,String] = Map()

  def consume(topic:String)= {

    val topic = "transactions"
    val KafkaConsumer = new KafkaConsumer[Int, String](props)
    KafkaConsumer.subscribe(util.Collections.singletonList(topic))
    try {
      while (true) {
        println("Yes")
        val consumerRecords = KafkaConsumer.poll(100)
        print(consumerRecords.records(topic))
        print(consumerRecords.partitions())
        if(consumerRecords != null ){

          for (c <- consumerRecords.iterator){
            println("Yes1")

            println(c.value())
          }

        }else {
          print("no records found")
        }


      }

    }catch {
      case e: NullPointerException =>
      {
        // Display this if exception is found
        println(e)
      }

    }
    finally {
      KafkaConsumer.close()
    }
  }



}
