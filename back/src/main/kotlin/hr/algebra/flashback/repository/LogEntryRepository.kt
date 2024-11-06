package hr.algebra.flashback.repository

import hr.algebra.flashback.model.log.LogAction
import hr.algebra.flashback.model.log.LogEntry
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

interface LogEntryRepository : ElasticsearchRepository<LogEntry, String> {
    fun findByUserId(userId: String): List<LogEntry>
    fun findByLogAction(logAction: LogAction): List<LogEntry>
}