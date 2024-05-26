package be.mbict.kafka2es.consumer.persistence.elasticsearch

import be.mbict.kafka2es.Data
import be.mbict.kafka2es.consumer.persistence.BulkRepository
import co.elastic.clients.elasticsearch.ElasticsearchClient
import org.springframework.stereotype.Component

// interface ElasticsearchDataRepository : ElasticsearchRepository<Data, String>

// @Component
class ElasticsearchBulkIndexer(private val elasticsearchClient: ElasticsearchClient) : BulkRepository<Data, String> {

    override fun bulkInsert(data: List<Data>) {
        val resp = elasticsearchClient.bulk {
            it.index("data")
            it.apply {
                data.forEach { d ->
                    operations {
                        it.index {
                            it.id("${d.id}")
                            it.document(d)
                        }
                    }
                }
            }
        }
        if (resp.errors()) println(resp.items().filter { it.error() != null })
    }
}
