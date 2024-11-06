package hr.algebra.flashback.controller

import hr.algebra.flashback.dto.user.ChangePlanDto
import hr.algebra.flashback.dto.user.UpdateUserDataDto
import hr.algebra.flashback.dto.user.UserDto
import hr.algebra.flashback.dto.user.toDto
import hr.algebra.flashback.model.log.LogAction
import hr.algebra.flashback.service.LogService
import hr.algebra.flashback.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    @Autowired
    private val userService: UserService,
    @Autowired
    private val logService: LogService
) {

    @GetMapping("/me")
    fun me(authUser: Authentication): ResponseEntity<UserDto> {
        return ResponseEntity.ok(userService.findOrCreateUser(authUser).toDto())
    }

    @PutMapping("/complete-profile")
    fun completeProfile(
        authUser: Authentication,
        @RequestBody updateUserDataDto: UpdateUserDataDto
    ): ResponseEntity<UserDto> {
        val updatedUser = userService.completeProfile(updateUserDataDto, authUser)
        logService.logAction(authUser.name, "Profile completed", LogAction.UPDATE_USER)
        return ResponseEntity.ok(updatedUser.toDto())
    }

    @PutMapping("/change-plan")
    fun changePlan(
        @RequestBody changePlanDto: ChangePlanDto,
        authUser: Authentication
    ): ResponseEntity<UserDto> {
        val updatedUser = userService.changeSubscriptionPlan(authUser, changePlanDto)
        logService.logAction(
            authUser.name,
            "Subscription plan changed to: ${updatedUser.subscriptionPlan}",
            LogAction.UPDATE_USER
        )
        return ResponseEntity.ok(updatedUser.toDto())
    }

    @DeleteMapping("/")
    fun deleteUser(
        authUser: Authentication
    ): ResponseEntity<Unit> {
        userService.deleteUser(authUser)
        logService.logAction(authUser.name, "User deleted", LogAction.DELETE_ACCOUNT)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}