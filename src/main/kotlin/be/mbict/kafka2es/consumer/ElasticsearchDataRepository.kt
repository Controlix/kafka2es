package be.mbict.kafka2es.consumer

import be.mbict.kafka2es.Data
import co.elastic.clients.elasticsearch.ElasticsearchClient
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Component

interface ElasticsearchDataRepository : ElasticsearchRepository<Data, String>

@Component
class ElesticsearchBulkIndexer(private val elasticsearchClient: ElasticsearchClient) {

    fun bulkIndex(data: List<Data>) {
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
