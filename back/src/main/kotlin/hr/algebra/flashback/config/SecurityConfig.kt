package hr.algebra.flashback.config

import hr.algebra.flashback.component.JwtConverter
import jakarta.servlet.http.HttpServletResponse
import org.keycloak.OAuth2Constants
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
    @Autowired
    private val jwtConverter: JwtConverter,
    @Value("\${keycloak.server.url}")
    private val keycloakServerUrl: String,
    @Value("\${keycloak.realm}")
    private val keycloakRealm: String,
    @Value("\${keycloak.admin.client.id}")
    private val keycloakAdminClientId: String,
    @Value("\${keycloak.admin.client.secret}")
    private val keycloakAdminClientSecret: String,
    @Value("\${keycloak.admin.client.username}")
    private val keycloakAdminUsername: String,
    @Value("\${keycloak.admin.client.password}")
    private val keycloakAdminPassword: String

) {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests { request ->
            request.requestMatchers("/api/v1/photos/**").permitAll()
            request.requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
            request.requestMatchers("/api/v1/statistics/**").hasRole("ADMIN")
            request.anyRequest().authenticated()
        }

        http.oauth2ResourceServer {
            oauth2 -> oauth2.jwt {
                token -> token.jwtAuthenticationConverter(jwtConverter) }
        }

        http.cors(Customizer.withDefaults())
        http.csrf { it.disable() }

        http.exceptionHandling { exceptionHandling ->
            exceptionHandling.authenticationEntryPoint { _, response, authException ->
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.message)
            }
            exceptionHandling.accessDeniedHandler { _, response, accessDeniedException ->
                response.sendError(HttpServletResponse.SC_FORBIDDEN, accessDeniedException.message)
            }
        }

        http.headers { headersConfigurer ->
            headersConfigurer.frameOptions {
                it.disable()
            }
            headersConfigurer.xssProtection {
                it.disable()
            }.disable()
        }

        return http.build()
    }

    @Bean
    fun auditorAware(): AuditorAware<String> {
        return ApplicationAuditAware()
    }

    @Bean
    fun keycloakAdminClient(): Keycloak {
        return KeycloakBuilder.builder()
            .serverUrl(keycloakServerUrl)
            .realm(keycloakRealm)
            .clientId(keycloakAdminClientId)
            .clientSecret(keycloakAdminClientSecret)
            .grantType(OAuth2Constants.PASSWORD)
            .username(keycloakAdminUsername)
            .password(keycloakAdminPassword)
            .build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("http://localhost:3000", "http://localhost:8080", "http://localhost:5173")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE")
        configuration.allowedHeaders = listOf("*")
        configuration.exposedHeaders = listOf("Content-Disposition")
        configuration.allowCredentials = true
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}