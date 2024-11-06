package hr.algebra.flashback.service

import hr.algebra.flashback.dto.user.ChangePlanDto
import hr.algebra.flashback.dto.user.CompleteProfileDto
import hr.algebra.flashback.exception.AlreadyChangedSubscriptionPlanException
import hr.algebra.flashback.exception.UserAlreadyCompletedException
import hr.algebra.flashback.exception.UserNotFoundException
import hr.algebra.flashback.model.user.SubscriptionPlan
import hr.algebra.flashback.model.user.User
import hr.algebra.flashback.repository.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.security.core.Authentication
import java.time.Clock
import java.time.LocalDate
import java.time.Month
import java.util.*

class UserServiceTest {
    private lateinit var userService: UserService
    private val userRepository: UserRepository = mock(UserRepository::class.java)
    private val photoService: PhotoService = mock(PhotoService::class.java)
    private val keycloakAdmin: KeycloakService = mock(KeycloakService::class.java)
    private lateinit var authUser: Authentication

    @BeforeEach
    fun setUp() {
        userService = UserService(userRepository, photoService, keycloakAdmin)

        `when`(userRepository.findById("1")).thenReturn(
            Optional.of(
                User("1", SubscriptionPlan.FREE, LocalDate.now(Clock.systemUTC()), false, 0)
            )
        )

        authUser = mock(Authentication::class.java)
        `when`(authUser.name).thenReturn("1")
    }

    @Test
    fun `test find user by id`() {
        val user = userService.findById("1")
        assert(user.id == "1")
    }

    @Test
    fun `test create user if not exists`() {
        val newAuthUser = mock(Authentication::class.java)
        `when`(newAuthUser.name).thenReturn("2")

        val newUser = User("2", SubscriptionPlan.FREE, LocalDate.now(Clock.systemUTC()), false, 0)

        `when`(userRepository.findById("2")).thenReturn(Optional.empty())
        `when`(userRepository.findById("2").orElseGet { userRepository.save(newUser) }).thenReturn(newUser)
        `when`(userRepository.save(
            User("2", SubscriptionPlan.FREE, LocalDate.now(Clock.systemUTC()), false, 0)))
            .thenReturn(User("2", SubscriptionPlan.FREE, LocalDate.now(Clock.systemUTC()), false, 0))

        val user = userService.findOrCreateUser(newAuthUser)
        assert(user.id == "2")
        assert(user.subscriptionPlan == SubscriptionPlan.FREE)
        assert(!user.isProfileCompleted)
        assert(user.dailyUpload == 0)
    }

    @Test
    fun `test get existing user`() {
        val user = userService.findOrCreateUser(authUser)
        assert(user.id == "1")
        assert(user.subscriptionPlan == SubscriptionPlan.FREE)
        assert(!user.isProfileCompleted)
        assert(user.dailyUpload == 0)
    }

    @Test
    fun `test complete profile for not existing user`() {
        val notExistingUser = mock(Authentication::class.java)
        `when`(notExistingUser.name).thenReturn("3")
        `when`(userRepository.findById("3")).thenReturn(Optional.empty())

        val completeProfileDto = CompleteProfileDto(SubscriptionPlan.GOLD)

        assertThrows<UserNotFoundException> {
            userService.completeProfile(completeProfileDto, notExistingUser)
        }
    }

    @Test
    fun `test complete profile when already completed`() {
        val completedUserAuth = mock(Authentication::class.java)
        val user = User("99", SubscriptionPlan.FREE, LocalDate.now(Clock.systemUTC()), true, 0)

        `when`(completedUserAuth.name).thenReturn("99")
        `when`(userRepository.findById("99")).thenReturn(Optional.of(user))

        val completeProfileDto = CompleteProfileDto(SubscriptionPlan.GOLD)

        assertThrows<UserAlreadyCompletedException> {
            userService.completeProfile(completeProfileDto, completedUserAuth)
        }
    }

    @Test
    fun `test complete profile`() {
        val user = User("1", SubscriptionPlan.FREE, LocalDate.now(Clock.systemUTC()), false, 0)
        val completeProfileDto = CompleteProfileDto(SubscriptionPlan.GOLD)

        `when`(userRepository.findById("1")).thenReturn(Optional.of(user))
        `when`(userRepository.save(user)).thenReturn(user)

        val updatedUser = userService.completeProfile(completeProfileDto, authUser)
        assert(updatedUser.subscriptionPlan == SubscriptionPlan.GOLD)
        assert(updatedUser.isProfileCompleted)
    }

    @Test
    fun `test reset daily upload for all user`(){
        val user1 = User("1", SubscriptionPlan.FREE, LocalDate.now(Clock.systemUTC()), false, 1)
        val user2 = User("2", SubscriptionPlan.FREE, LocalDate.now(Clock.systemUTC()), false, 2)
        val user3 = User("3", SubscriptionPlan.FREE, LocalDate.now(Clock.systemUTC()), false, 3)


        `when`(userRepository.findAll()).thenReturn(listOf(user1, user2, user3))
        `when`(userRepository.save(user1)).thenReturn(user1)
        `when`(userRepository.save(user2)).thenReturn(user2)
        `when`(userRepository.save(user3)).thenReturn(user3)

        userService.resetDailyUploadForAllUsers()

        assert(user1.dailyUpload == 0)
        assert(user2.dailyUpload == 0)
        assert(user3.dailyUpload == 0)
    }

    @Test
    fun `test delete user when not found`() {
        val notExistingUser = mock(Authentication::class.java)
        `when`(notExistingUser.name).thenReturn("99")
        `when`(userRepository.findById("99")).thenReturn(Optional.empty())

        assertThrows<UserNotFoundException> {
            userService.deleteUser(notExistingUser)
        }
    }

    @Test
    fun `test delete user when deleting another user`() {
        val anotherUser = mock(Authentication::class.java)
        `when`(anotherUser.name).thenReturn("99")
        val user = User("1", SubscriptionPlan.FREE, LocalDate.now(Clock.systemUTC()), false, 0)

        `when`(userRepository.findById("1")).thenReturn(Optional.of(user))

        assertThrows<UserNotFoundException> {
            userService.deleteUser(anotherUser)
        }
    }

    @Test
    fun `test delete user`() {
        val user = User("1", SubscriptionPlan.FREE, LocalDate.now(Clock.systemUTC()), false, 0)

        `when`(userRepository.findById("1")).thenReturn(Optional.of(user))
        `when`(userRepository.deleteById(user.id)).then {
            `when`(userRepository.findById("1")).thenReturn(Optional.empty())
        }

        userService.deleteUser(authUser)

        assertThrows<UserNotFoundException> {
            userService.findById("1")
        }
    }

    @Test
    fun `test change subscription plan when not found`(){
        val notExistingUser = mock(Authentication::class.java)
        `when`(notExistingUser.name).thenReturn("99")
        `when`(userRepository.findById("99")).thenReturn(Optional.empty())

        val changePlanDto = ChangePlanDto(SubscriptionPlan.GOLD)

        assertThrows<UserNotFoundException> {
            userService.changeSubscriptionPlan(changePlanDto, notExistingUser)
        }
    }

    @Test
    fun `test change subscription plan when last change was less than 24 hours ago`(){
        val user = User("1", SubscriptionPlan.FREE, LocalDate.now(Clock.systemUTC()), false, 0)

        `when`(userRepository.findById("1")).thenReturn(Optional.of(user))

        val changePlanDto = ChangePlanDto(SubscriptionPlan.GOLD)

        assertThrows<AlreadyChangedSubscriptionPlanException> {
            userService.changeSubscriptionPlan(changePlanDto, authUser)
        }
    }

    @Test
    fun `test change subscription plan`(){
        val user = User("1", SubscriptionPlan.FREE, LocalDate.of(2024, Month.NOVEMBER, 1), false, 0)

        `when`(userRepository.findById("1")).thenReturn(Optional.of(user))
        `when`(userRepository.save(user)).thenReturn(user)

        val changePlanDto = ChangePlanDto(SubscriptionPlan.GOLD)

        val updatedUser = userService.changeSubscriptionPlan(changePlanDto, authUser)
        assert(updatedUser.subscriptionPlan == SubscriptionPlan.GOLD)
    }
}