package be.mbict.kafka2es

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document

@Document(indexName = "data")
data class Data @JsonCreator constructor(
    @JsonProperty("id") @Id val id: Int,
    @JsonProperty("name") val name: String,
    @JsonProperty("job") val job: String,
    @JsonProperty("address") val address: String
)