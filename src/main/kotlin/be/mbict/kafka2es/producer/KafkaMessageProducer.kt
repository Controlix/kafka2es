package be.mbict.kafka2es.producer

import be.mbict.kafka2es.Data
import net.datafaker.Faker
import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import org.jeasy.random.FieldPredicates
import org.springframework.kafka.core.KafkaTemplate

class KafkaMessageProducer(private val kafkaTemplate: KafkaTemplate<Int, Data>) {

    private val easyRandom = EasyRandom(EasyRandomParameters()
        .randomize(FieldPredicates.named("name"), Faker().name()::fullName)
        .randomize(FieldPredicates.named("job"), Faker().job()::title)
        .randomize(FieldPredicates.named("address"), Faker().address()::fullAddress)
    )

    fun sendRandomData(id: Int) {
        val data = easyRandom.nextObject(Data::class.java)
        kafkaTemplate.send("data", id, data.copy( id = id)).whenComplete { res, ex ->  if (ex != null) println(ex)}
    }
}