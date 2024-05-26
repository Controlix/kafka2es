package be.mbict.kafka2es.consumer

import be.mbict.kafka2es.Data
import be.mbict.kafka2es.consumer.persistence.BulkRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import org.springframework.util.StopWatch
import kotlin.time.Duration.Companion.seconds

@Component
class KafkaMessageListener(private val dataRepository: CrudRepository<Data, String>, private val bulkRepository: BulkRepository<Data, String>) {

    // @KafkaListener(id = "data-group", topics = ["data"])
    fun receiveOneMessageAndSaveIt(data: Data): Unit { // 1
        time()
        dataRepository.save(data)
        processedMessages++
        time()
    }

    // @KafkaListener(id = "data-group", topics = ["data"], batch = "true")
    fun receiveBatchOfMessagesAndSaveThemOneByOne(data: List<Data>): Unit { // 2
        time()
        data.forEach(dataRepository::save)
        processedMessages += data.size
        time()
    }

    // @KafkaListener(id = "data-group", topics = ["data"])
    fun receiveMessagesOneByOneAndBufferThemToSaveThemInBatch(data: Data): Unit { // 3
        time()
        dataBuffer.add(data)
        if (dataBuffer.size >= 1000) {
            dataRepository.saveAll(dataBuffer)
            dataBuffer.clear()
        }
        processedMessages++
        time()
    }

    // @KafkaListener(id = "data-group", topics = ["data"], batch = "true")
    fun receiveBatchOfMessagesAndSaveThemAll(data: List<Data>): Unit { // 4
        time()
        dataRepository.saveAll(data)
        processedMessages += data.size
        time()
    }

    @KafkaListener(id = "data-group", topics = ["data"], batch = "true")
    fun receiveBatchOfMessagesAndSaveThemInBulk(data: List<Data>): Unit { // 5
        time()
        bulkRepository.bulkInsert(data)
        processedMessages += data.size
        time()
    }

    companion object {
        var processedMessages: Int = 0
        val dataBuffer: MutableList<Data> = mutableListOf()
        val MAX_MSG = 1_000_000

        private val stopWatch = StopWatch()

        fun time() {
            when (processedMessages) {
                0 -> {
                    println("Start processing messages...")
                    stopWatch.start()
                }
                MAX_MSG -> {
                    stopWatch.stop()
                    println("Processed 10^${Math.log10(processedMessages * 1.0).toInt()} messages in ${stopWatch.totalTimeSeconds.seconds.toIsoString()}")
                }
            }
        }
    }
}
