package hr.algebra.flashback.service

import hr.algebra.flashback.dto.auth.UpdateUserDataDto
import hr.algebra.flashback.model.user.Role
import hr.algebra.flashback.model.user.SubscriptionPlan
import hr.algebra.flashback.repository.UserRepository
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.annotation.Transactional

@Transactional
class UserAdminServiceTest : AbstractBaseContainerTest() {

    @Autowired
    private lateinit var userAdminService: UserAdminService

    @Autowired
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setUp() {
        userRepository.deleteAll()
        userRepository.saveAll(
            listOf(
                User(
                    firstName = "John",
                    lastName = "Doe",
                    flashbackUsername = "johndoe",
                    role = Role.ADMIN
                ),
                User(
                    firstName = "Jane",
                    lastName = "Doe",
                    flashbackUsername = "janedoe",
                    role = Role.USER
                )
            )
        )
    }

    @Test
    fun findById() {
        val user = userAdminService.findById(1)
        assertEquals("John", user.firstName)
        assertEquals("Doe", user.lastName)
        assertEquals("johndoe", user.username)
        assertEquals(Role.ADMIN, user.role)
    }

    @Test
    fun getCurrentUploadConsumption() {
    }

    @Test
    fun updateUserData() {
        val updatedUser = userAdminService.updateUserData(
            1,
            UpdateUserDataDto(
                firstName = "Johny",
                lastName = "Doe",
                subscriptionPlan = SubscriptionPlan.PRO,
                role = Role.USER
            )
        )
        assertEquals("Johny", updatedUser.firstName)
        assertEquals("Doe", updatedUser.lastName)
        assertEquals(SubscriptionPlan.PRO, updatedUser.subscriptionPlan)
        assertEquals(Role.USER, updatedUser.role)
    }

    @Test
    fun deleteUser() {
        val authAdmin = userRepository.findById(1).get()
        userAdminService.deleteUser(1, authAdmin)
        val users = userRepository.findAll()
        assertEquals(1, users.toList().size)
    }
}