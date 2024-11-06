package hr.algebra.flashback.service

import hr.algebra.flashback.dto.user.*
import hr.algebra.flashback.exception.UserNotFoundException
import hr.algebra.flashback.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserAdminService(
    @Autowired
    private val userRepository: UserRepository,
    @Autowired
    private val photoAdminService: PhotoAdminService,
    @Autowired
    private val keycloakService: KeycloakService
) {
    fun findPaginatedUsers(pageable: PageRequest, query: String?, authAdmin: Authentication): Page<KeycloakUser> {
        val users = getFullySetupedKeycloakUsers()

        return users
            .filter { it.username.contains(query ?: "", ignoreCase = true) }
            .filter { it.id != authAdmin.name }
            .map { it }
            .let { PageImpl(it, pageable, it.size.toLong()) }
    }

    fun findAll(): List<KeycloakUser> {
        return getFullySetupedKeycloakUsers()
    }

    fun findById(id: String): KeycloakUser {
        return keycloakService.getUsers()
            .find { it.id == id }
            ?: throw UserNotFoundException("User with id $id not found")
    }

    fun findUserDetailsById(id: String): UserDto {
        return userRepository.findById(id)
            .map { it.toDto() }
            .orElseThrow { UserNotFoundException("User with id $id not found") }
    }

    fun updateUserData(id: String, modifyData: UpdateUserDataDto, authAdmin: Authentication): UserDto {
        val user = userRepository
            .findById(id)
            .orElseThrow{UserNotFoundException("User with id $id not found")}

        user.subscriptionPlan = modifyData.subscriptionPlan
        user.dailyUpload = if (modifyData.resetDailyUploads) 0 else user.dailyUpload
        user.isProfileCompleted = true
        userRepository.save(user)

        keycloakService.updateUser(id, modifyData)

        return user.toDto()
    }

    @Transactional
    fun deleteUser(id: String, authAdmin: Authentication) {
        if (id == authAdmin.name) {
            throw IllegalArgumentException("You cannot delete yourself")
        }
        photoAdminService.deleteAllPhotosByUser(id)
        userRepository.deleteById(id)
        keycloakService.deleteUser(id)
    }

    private fun getFullySetupedKeycloakUsers(): List<KeycloakUser>{
        val keycloakUsers = keycloakService.getUsers()
        val flashbackUserIds = userRepository.findAll().map { it.id }.toSet()
        
        return keycloakUsers
            .filter {
                flashbackUserIds.contains(it.id)
            }
    }


}