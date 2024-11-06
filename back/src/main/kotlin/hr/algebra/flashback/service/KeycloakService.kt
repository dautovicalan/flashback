package hr.algebra.flashback.service

import hr.algebra.flashback.dto.user.KeycloakUser
import hr.algebra.flashback.dto.user.UpdateUserDataDto
import org.keycloak.admin.client.Keycloak
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class KeycloakService(
    @Autowired
    private val keycloak: Keycloak,
    @Value("\${keycloak.realm}")
    private val realm: String
) {

    fun getUsers(): List<KeycloakUser> {
        return keycloak.realm(realm).users().list().map { user ->
            KeycloakUser(
                id = user.id,
                username = user.username,
                email = user.email,
                firstName = user.firstName,
                lastName = user.lastName,
            )
        }
    }

    fun deleteUser(id: String) {
        keycloak.realm(realm).users().get(id).remove()
    }

    fun updateUser(id: String, modifyData: UpdateUserDataDto) {
        keycloak.realm(realm).users().get(id).update(
            UserRepresentation().apply {
                firstName = modifyData.firstName
                lastName = modifyData.lastName
            }
        )
    }
}