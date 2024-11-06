package hr.algebra.flashback.service

import hr.algebra.flashback.exception.LogActionNotFoundException
import hr.algebra.flashback.exception.UserNotFoundException
import hr.algebra.flashback.logging.Logger
import hr.algebra.flashback.model.log.LogAction
import hr.algebra.flashback.model.log.LogEntry
import hr.algebra.flashback.repository.LogEntryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LogService(
    @Autowired
    private val logEntryRepository: LogEntryRepository,
    @Autowired
    private val userService: UserService,
    private val logger: Logger
) {

    fun logAction(userId: String, action: String, logAction: LogAction) {
        logger.info("User $userId performed action $action")
        logEntryRepository.save(LogEntry(
            userId = userId,
            action = action,
            logAction = logAction
        ))
    }

    fun findActionLogs(): List<LogEntry> {
        return logEntryRepository.findAll().toList()
    }

    fun findUserLogs(userId: String): List<LogEntry> {
        val user = userService.findById(userId)
        return logEntryRepository.findByUserId(user.id)
    }

    fun findActionLogsByAction(action: String): List<LogEntry> {
        if (LogAction.entries.toTypedArray().none { it.name == action }) {
            throw LogActionNotFoundException("Invalid action: $action")
        }
        return logEntryRepository.findByLogAction(LogAction.valueOf(action))
    }

    fun deleteLogsByUser(userId: String) {
        val user = userService.findById(userId)

        logEntryRepository.findByUserId(user.id).forEach {
            logEntryRepository.delete(it)
        }

        logAction(userId, "Logs deleted", LogAction.DELETE_LOGS)
    }
}