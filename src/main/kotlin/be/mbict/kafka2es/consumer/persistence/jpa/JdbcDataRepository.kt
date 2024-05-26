package be.mbict.kafka2es.consumer.persistence.jpa

import be.mbict.kafka2es.Data
import be.mbict.kafka2es.consumer.persistence.BulkRepository
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Transactional
interface JdbcDataRepository : JpaRepository<Data, String>

@Transactional
@Component
class MariaDbMultiRowsInsert(private val jdbcTemplate: JdbcTemplate) : BulkRepository<Data, String> {

    override fun bulkInsert(data: List<Data>) {
        val values = data.map { """("${it.id}", "${it.name}", "${it.job}", "${it.address}")""" }.joinToString(",")
        val stmt = "insert into data(id, name, job, address) values $values"
//        println(stmt)
        jdbcTemplate.update(stmt)
    }

}