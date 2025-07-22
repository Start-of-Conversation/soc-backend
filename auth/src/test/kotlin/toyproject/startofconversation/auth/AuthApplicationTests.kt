package toyproject.startofconversation.auth

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("auth")
@SpringBootTest(classes = [AuthApplication::class])
class AuthApplicationTests {

    @Test
    fun contextLoads() {
    }

    @Test
    fun checkProperty() {
        println(">> " + System.getProperty("social.apple.auth.public-key-url"))
        println(">> " + System.getenv("SOCIAL_APPLE_AUTH_PUBLIC_KEY_URL"))
    }

}
