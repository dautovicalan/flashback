package hr.algebra.flashback.component

import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.stereotype.Component


// ADAPTER PATTERN
@Component
class JwtConverter: Converter<Jwt, AbstractAuthenticationToken> {

    override fun convert(jwt: Jwt): AbstractAuthenticationToken {
        return JwtAuthenticationToken(
            jwt,
            JwtGrantedAuthoritiesConverter().convert(jwt)?.let {
                sequenceOf(
                    it.asSequence(),
                    extractResourceRoles(jwt).asSequence()
                ).flatten().toSet()
            }
        )
    }

    private fun extractResourceRoles(jwt: Jwt): Collection<GrantedAuthority> {
        val realmAccess = jwt.getClaim<Map<String, List<String>>>("realm_access") ?: emptyMap()
        val roles = realmAccess["roles"] as List<String>

        return roles.map { role -> SimpleGrantedAuthority("ROLE_" + role.replace("-", "_")) }.toSet()
    }
}