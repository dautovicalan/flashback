package hr.algebra.flashback.controller

import hr.algebra.flashback.model.log.LogEntry
import hr.algebra.flashback.service.LogService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/statistics")
class StatisticController(
    @Autowired
    private val logService: LogService
) {
    @GetMapping("/logs")
    fun getActionLogs(): ResponseEntity<List<LogEntry>> {
        return ResponseEntity.ok(logService.findActionLogs())
    }

    @GetMapping("/logs/{userId}")
    fun getActionLogsByUser(@PathVariable userId: String): ResponseEntity<List<LogEntry>> {
        return ResponseEntity.ok(logService.findUserLogs(userId))
    }

    @GetMapping("/logs/action/{action}")
    fun getActionLogsByAction(@PathVariable action: String): ResponseEntity<List<LogEntry>> {
        return ResponseEntity.ok(logService.findActionLogsByAction(action))
    }

    @DeleteMapping("/logs/{userId}")
    fun deleteLogsByUser(@PathVariable userId: String): ResponseEntity<Unit> {
        logService.deleteLogsByUser(userId)
        return ResponseEntity.ok().build()
    }
}