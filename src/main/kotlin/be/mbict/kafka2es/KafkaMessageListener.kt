package be.mbict.kafka2es

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class KafkaMessageListener(private val elasticsearchDataRepository: ElasticsearchDataRepository) {

    @KafkaListener(id = "data-group", topics = ["data"])
    fun receiveMessage(data: Data): Unit {
        println("Got message $data")
        elasticsearchDataRepository.save(data)
    }
}

@Document(indexName = "data")
data class Data(
    @Id val id: Int,
    val name: String
)