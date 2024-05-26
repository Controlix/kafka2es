package be.mbict.kafka2es.consumer.persistence

interface BulkRepository<T, ID> {
    fun bulkInsert(data: List<T>)
}