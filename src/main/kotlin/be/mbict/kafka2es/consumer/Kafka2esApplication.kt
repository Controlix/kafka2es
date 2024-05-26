package be.mbict.kafka2es.consumer

import be.mbict.kafka2es.Data
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication

@EntityScan(basePackageClasses = [Data::class])
@SpringBootApplication
class Kafka2esApplication

fun main(args: Array<String>) {
	runApplication<Kafka2esApplication>(*args)
}
