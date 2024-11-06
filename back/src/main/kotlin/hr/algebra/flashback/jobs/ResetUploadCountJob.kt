package hr.algebra.flashback.jobs

import hr.algebra.flashback.logging.Logger
import hr.algebra.flashback.service.LogService
import hr.algebra.flashback.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ResetUploadCountJob(
    @Autowired
    private val userService: UserService,
    @Autowired
    private val logger: Logger
) {

    @Scheduled(cron = "0 0 0 * * *")
    fun resetUploadCount() {
        logger.info("Resetting daily upload count for all users")
        userService.resetDailyUploadForAllUsers()
    }
}