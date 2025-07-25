package toyproject.startofconversation.api.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import toyproject.startofconversation.auth.jwt.JwtAuthFilter
import toyproject.startofconversation.auth.jwt.JwtAuthenticationEntryPoint
import toyproject.startofconversation.auth.jwt.config.JwtConfig
import toyproject.startofconversation.common.domain.user.entity.value.Role

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(JwtConfig::class)
class SecurityConfig(
    private val jwtAuthFilter: JwtAuthFilter,
    private val entryPoint: JwtAuthenticationEntryPoint
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .exceptionHandling { handler -> handler.authenticationEntryPoint(entryPoint) }
            .authorizeHttpRequests {
                it.requestMatchers("/auth/logout", "/auth/local/password").authenticated()
                it.requestMatchers("/api/admin/**").hasRole(Role.ADMIN.name)
                it.requestMatchers(
                    "/auth/**",
                    "/api/*/public",
                    "/api/*/public/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/health",
                    "/v3/api-docs/**",
                    "/v3/api-docs.yaml"
                ).permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}