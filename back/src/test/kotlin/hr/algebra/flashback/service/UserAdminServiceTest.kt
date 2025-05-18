package hr.algebra.flashback.service

import hr.algebra.flashback.dto.user.KeycloakUser
import hr.algebra.flashback.exception.UserNotFoundException
import hr.algebra.flashback.model.user.SubscriptionPlan
import hr.algebra.flashback.model.user.User
import hr.algebra.flashback.repository.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.keycloak.admin.client.Keycloak
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.security.core.Authentication
import java.time.LocalDate

class UserAdminServiceTest {

    private lateinit var userAdminService: UserAdminService
    private val userRepository: UserRepository = mock(UserRepository::class.java)
    private val photoAdminService: PhotoAdminService = mock(PhotoAdminService::class.java)
    private val keycloakService: KeycloakService = mock(KeycloakService::class.java)
    private val keycloak: Keycloak = mock(Keycloak::class.java)

    private val realm: String = "test-realm"

    @BeforeEach
    fun setUp() {
        `when`(keycloakService.getUsers()).thenReturn(listOf(
            KeycloakUser(
                id = "1",
                username = "testuser",
                email = "test@test.com",
                firstName = "Test",
                lastName = "User"
            ),
        ))
        `when`(userRepository.findAll()).thenReturn(listOf(
            User(
                id = "1",
                subscriptionPlan = SubscriptionPlan.GOLD,
                lastSubscriptionChange = LocalDate.now(),
                isProfileCompleted = true,
                dailyUpload = 0
            )
        ))
        userAdminService = UserAdminService(userRepository, photoAdminService, keycloakService)
    }

    @Test
    fun `test find all users`() {
        val users = userAdminService.findAll()
        assert(users.isNotEmpty())
        assert(users[0].username == "testuser")
    }

    @Test
    fun `test find user by id`() {
        val user = userAdminService.findById("1")
        assert(user.id == "1")
        assert(user.username == "testuser")
    }

    @Test
    fun `test throw exception when user not found`() {
        assertThrows<UserNotFoundException> {
            userAdminService.findById("99")
        }
    }

    @Test
    fun `test you cannot delete yourself`() {
        val authAdmin = mock(Authentication::class.java)
        `when`(authAdmin.name).thenReturn("1")

        assertThrows<IllegalArgumentException> {
            userAdminService.deleteUser("1", authAdmin)
        }
    }
}