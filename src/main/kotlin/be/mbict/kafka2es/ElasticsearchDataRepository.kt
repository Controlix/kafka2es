package be.mbict.kafka2es

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

interface ElasticsearchDataRepository: ElasticsearchRepository<Data, String>