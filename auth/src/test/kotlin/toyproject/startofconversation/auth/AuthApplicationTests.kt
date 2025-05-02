package toyproject.startofconversation.auth

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
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
