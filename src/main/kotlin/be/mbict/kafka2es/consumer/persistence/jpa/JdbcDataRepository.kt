package be.mbict.kafka2es.consumer.persistence.jpa

import be.mbict.kafka2es.Data
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository

@Transactional
interface JdbcDataRepository : JpaRepository<Data, String>