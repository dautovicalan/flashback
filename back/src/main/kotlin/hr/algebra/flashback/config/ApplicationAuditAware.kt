package hr.algebra.flashback.config

import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

class ApplicationAuditAware : AuditorAware<String> {
    override fun getCurrentAuditor(): Optional<String> {
        val authentication = SecurityContextHolder.getContext().authentication

        if (authentication == null || !authentication.isAuthenticated) {
            return Optional.empty()
        }

        return Optional.of(authentication.name)
    }
}