package hr.algebra.flashback.service

import hr.algebra.flashback.dto.user.ChangePlanDto
import hr.algebra.flashback.dto.user.CompleteProfileDto
import hr.algebra.flashback.exception.AlreadyChangedSubscriptionPlanException
import hr.algebra.flashback.exception.UserAlreadyCompletedException
import hr.algebra.flashback.exception.UserNotFoundException
import hr.algebra.flashback.model.user.SubscriptionPlan
import hr.algebra.flashback.model.user.User
import hr.algebra.flashback.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.time.LocalDate

@Service
class UserService(
    @Autowired
    private val userRepository: UserRepository,
    @Autowired
    private val photoService: PhotoService,
    @Autowired
    private val keycloakAdmin: KeycloakService
) {

    fun findById(createdBy: String): User {
        return userRepository.findById(createdBy)
            .orElseThrow { UserNotFoundException("User with $createdBy not found") }
    }

    @Transactional
    fun findOrCreateUser(authUser: Authentication): User {
        return userRepository.findById(authUser.name)
            .orElseGet {
                val user = User(
                    authUser.name,
                    SubscriptionPlan.FREE,
                    LocalDate.now(Clock.systemUTC()),
                    false,
                    0)
                userRepository.save(user)
            }
    }

    fun completeProfile(completeProfileDto: CompleteProfileDto, authUser: Authentication): User {
        val user = userRepository.findById(authUser.name)
            .orElseThrow { UserNotFoundException("User with ${authUser.name} not found") }

        if (user.isProfileCompleted) {
            throw UserAlreadyCompletedException("User with ${authUser.name} already completed profile")
        }

        user.subscriptionPlan = completeProfileDto.subscriptionPlan
        user.isProfileCompleted = true
        return userRepository.save(user)
    }

    @Transactional
    fun increaseDailyUpload(authUser: Authentication) {
        val user = userRepository.findById(authUser.name)
            .orElseThrow { UserNotFoundException("User with ${authUser.name} not found") }

        user.dailyUpload++
        userRepository.save(user)
    }

    @Transactional
    fun resetDailyUploadForAllUsers() {
        userRepository.findAll().forEach {
            it.dailyUpload = 0
            userRepository.save(it)
        }
    }

    @Transactional
    fun deleteUser(authUser: Authentication) {
        val user = userRepository.findById(authUser.name)
            .orElseThrow { UserNotFoundException("User with ${authUser.name} not found") }

        if (user.id != authUser.name) {
            throw UserNotFoundException("User with ${authUser.name} not found")
        }

        photoService.deleteAllPhotosByUser(authUser)
        userRepository.deleteById(authUser.name)

        keycloakAdmin.deleteUser(authUser.name)
    }

    fun changeSubscriptionPlan(changePlanDto: ChangePlanDto, authUser: Authentication): User {
        val user = userRepository.findById(authUser.name)
            .orElseThrow { UserNotFoundException("User with ${authUser.name} not found") }

        if (user.lastSubscriptionChange.plusDays(1) > LocalDate.now(Clock.systemUTC())) {
            throw AlreadyChangedSubscriptionPlanException("Subscription plan can be changed only once a day")
        }

        user.subscriptionPlan = changePlanDto.subscriptionPlan
        user.lastSubscriptionChange = LocalDate.now(Clock.systemUTC())
        return userRepository.save(user)
    }
}