package be.mbict.kafka2es.consumer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Kafka2esApplication

fun main(args: Array<String>) {
	runApplication<Kafka2esApplication>(*args)
}
