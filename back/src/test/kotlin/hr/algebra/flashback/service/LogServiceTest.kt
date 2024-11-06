package hr.algebra.flashback.service

import hr.algebra.flashback.exception.LogActionNotFoundException
import hr.algebra.flashback.logging.Logger
import hr.algebra.flashback.model.log.LogAction
import hr.algebra.flashback.model.log.LogEntry
import hr.algebra.flashback.model.user.SubscriptionPlan
import hr.algebra.flashback.model.user.User
import hr.algebra.flashback.repository.LogEntryRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import java.time.Clock
import java.time.LocalDate

class LogServiceTest {

    private val logEntryRepository: LogEntryRepository = mock(LogEntryRepository::class.java)
    private val userService: UserService = mock(UserService::class.java)
    private val logger: Logger = mock(Logger::class.java)
    private lateinit var logService: LogService


    @BeforeEach
    fun setUp() {
        logService = LogService(logEntryRepository, userService, logger)
    }

    @Test
    fun `test log action`() {
        val logEntry = LogEntry("1", "1", "action", System.currentTimeMillis(), LogAction.UPDATE_PHOTO)
        `when`(logEntryRepository.save(any())).thenReturn(logEntry)
        `when`(userService.findById("1")).thenReturn(
            User(
                "1",
                SubscriptionPlan.FREE,
                LocalDate.now(Clock.systemUTC()),
                true,
                0
            )
        )
        `when`(logEntryRepository.findByUserId("1")).thenReturn(listOf(logEntry))
        logService.logAction("1", "action", LogAction.UPDATE_PHOTO)

        val logs = logService.findUserLogs("1")
        assert(logs.isNotEmpty())
        assert(logs[0].action == "action")
        assert(logs[0].logAction == LogAction.UPDATE_PHOTO)
    }

    @Test
    fun `test find action logs`() {
        val logEntry = LogEntry("1", "1", "action", System.currentTimeMillis(), LogAction.UPDATE_PHOTO)
        `when`(logEntryRepository.findAll()).thenReturn(listOf(logEntry))
        val logs = logService.findActionLogs()
        assert(logs.isNotEmpty())
        assert(logs[0].action == "action")
        assert(logs[0].logAction == LogAction.UPDATE_PHOTO)
    }

    @Test
    fun `test find user logs`() {
        val logEntry = LogEntry("1", "1", "action", System.currentTimeMillis(), LogAction.UPDATE_PHOTO)
        `when`(userService.findById("1")).thenReturn(
            User(
                "1",
                SubscriptionPlan.FREE,
                LocalDate.now(Clock.systemUTC()),
                true,
                0
            )
        )
        `when`(logEntryRepository.findByUserId("1")).thenReturn(listOf(logEntry))
        val logs = logService.findUserLogs("1")
        assert(logs.isNotEmpty())
        assert(logs[0].action == "action")
        assert(logs[0].logAction == LogAction.UPDATE_PHOTO)
    }

    @Test
    fun `test find action when action is not valid`() {
        assertThrows<LogActionNotFoundException> {
            logService.findActionLogsByAction("INVALID_ACTION")
        }
    }

    @Test
    fun `test find action logs by action`() {
        val logEntry = LogEntry("1", "1", "action", System.currentTimeMillis(), LogAction.UPDATE_PHOTO)
        `when`(logEntryRepository.findByLogAction(LogAction.UPDATE_PHOTO)).thenReturn(listOf(logEntry))
        val logs = logService.findActionLogsByAction("UPDATE_PHOTO")
        assert(logs.isNotEmpty())
        assert(logs[0].action == "action")
        assert(logs[0].logAction == LogAction.UPDATE_PHOTO)
    }

    @Test
    fun `test delete logs by user`() {
        val logEntry = LogEntry("1", "1", "action", System.currentTimeMillis(), LogAction.UPDATE_PHOTO)
        `when`(userService.findById("1")).thenReturn(
            User(
                "1",
                SubscriptionPlan.FREE,
                LocalDate.now(Clock.systemUTC()),
                true,
                0
            )
        )
        `when`(logEntryRepository.findByUserId("1")).thenReturn(listOf(logEntry))
        `when`(logEntryRepository.delete(any())).then {
            `when`(logEntryRepository.findByUserId("1")).thenReturn(listOf())
        }
        logService.deleteLogsByUser("1")

        val logs = logService.findUserLogs("1")
        assert(logs.isEmpty())
    }
}