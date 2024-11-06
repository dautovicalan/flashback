package hr.algebra.flashback.service

import hr.algebra.flashback.repository.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock

class UserAdminServiceTest {

    private lateinit var userAdminService: UserAdminService
    private val userRepository: UserRepository = mock(UserRepository::class.java)
    private val photoAdminService: PhotoAdminService = mock(PhotoAdminService::class.java)
    private val keycloakService: KeycloakService = mock(KeycloakService::class.java)

    @BeforeEach
    fun setUp() {
        userAdminService = UserAdminService(userRepository, photoAdminService, keycloakService)
    }
}