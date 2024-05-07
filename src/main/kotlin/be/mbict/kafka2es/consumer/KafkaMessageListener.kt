package be.mbict.kafka2es.consumer

import be.mbict.kafka2es.Data
import org.apache.commons.io.FileUtils
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import org.springframework.util.StopWatch
import kotlin.time.Duration.Companion.seconds

@Component
class KafkaMessageListener(private val elasticsearchDataRepository: ElasticsearchDataRepository, private val elasticsearchBulkIndexer: ElesticsearchBulkIndexer) {

    // @KafkaListener(id = "data-group", topics = ["data"])
    fun receiveOneMessageAndSaveIt(data: Data): Unit {
        time()
        elasticsearchDataRepository.save(data)
        processedMessages++
        time()
    }

    // @KafkaListener(id = "data-group", topics = ["data"], batch = "true")
    fun receiveBatchOfMessagesAndSaveThemOneByOne(data: List<Data>): Unit {
        time()
        data.forEach(elasticsearchDataRepository::save)
        processedMessages += data.size
        time()
    }

    @KafkaListener(id = "data-group", topics = ["data"])
    fun receiveMessagesOneByOneAndBufferThemToSaveThemInBatch(data: Data): Unit {
        time()
        dataBuffer.add(data)
        if (dataBuffer.size >= 1000) {
            elasticsearchDataRepository.saveAll(dataBuffer)
            dataBuffer.clear()
        }
        processedMessages++
        time()
    }

    // @KafkaListener(id = "data-group", topics = ["data"], batch = "true")
    fun receiveBatchOfMessagesAndSaveThemAll(data: List<Data>): Unit {
        time()
        elasticsearchDataRepository.saveAll(data)
        processedMessages += data.size
        time()
    }

    // @KafkaListener(id = "data-group", topics = ["data"], batch = "true")
    fun receiveBatchOfMessagesAndSaveThemInBulk(data: List<Data>): Unit {
        time()
        elasticsearchBulkIndexer.bulkIndex(data)
        processedMessages += data.size
        time()
    }

    companion object {
        var processedMessages: Int = 0
        val dataBuffer: MutableList<Data> = mutableListOf()

        private val stopWatch = StopWatch()

        fun time() {
            when (processedMessages) {
                0 -> {
                    println("Start processing messages...")
                    stopWatch.start()
                }
                100_000 -> {
                    stopWatch.stop()
                    println("Processed 10^${Math.log10(processedMessages * 1.0).toInt()} messages in ${stopWatch.totalTimeSeconds.seconds.toIsoString()}")
                }
            }
        }
    }
}