package hr.algebra.flashback.dto.user

data class KeycloakUser(
    val id: String,
    val username: String,
    val email: String?,
    val firstName: String,
    val lastName: String,
)