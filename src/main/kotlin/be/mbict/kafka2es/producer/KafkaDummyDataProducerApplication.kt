package be.mbict.kafka2es.producer

import be.mbict.kafka2es.Data
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories
import org.springframework.kafka.core.KafkaTemplate
import java.time.LocalDateTime

@SpringBootApplication
@EnableElasticsearchRepositories(basePackageClasses = [KafkaDummyDataProducerApplication::class])
class KafkaDummyDataProducerApplication : CommandLineRunner {

    @Autowired
    lateinit var kafkaMessageProducer: KafkaMessageProducer

    override fun run(vararg args: String?) {
        println("${LocalDateTime.now()}: Start...")
        (1..MAX_MSG).forEach(kafkaMessageProducer::sendRandomData)
        println("${LocalDateTime.now()}: Done!")
    }

    companion object {
        val MAX_MSG = 1_000_000
    }
}

fun main(args: Array<String>) {
    runApplication<KafkaDummyDataProducerApplication>(*args).close()
}

@Configuration
internal class KafkaDummyDataProducerConfig {

    @Autowired
    lateinit var kafkaTemplate: KafkaTemplate<Int, Data>

    @Bean
    fun dataProducer() = KafkaMessageProducer(kafkaTemplate)
}