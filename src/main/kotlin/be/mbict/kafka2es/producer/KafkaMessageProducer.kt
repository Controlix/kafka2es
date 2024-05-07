package be.mbict.kafka2es.producer

import be.mbict.kafka2es.Data
import org.springframework.kafka.core.KafkaTemplate
import kotlin.random.Random

class KafkaMessageProducer(private val kafkaTemplate: KafkaTemplate<Int, Data>) {

    fun sendRandomData(id: Int) = kafkaTemplate.send("data", id, Data(id = id, name = Random.nextBytes(5).toString())).whenComplete { res, ex ->  if (ex != null) println(ex)}
}