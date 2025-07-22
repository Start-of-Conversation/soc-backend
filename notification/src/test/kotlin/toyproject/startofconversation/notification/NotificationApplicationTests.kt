package toyproject.startofconversation.notification

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [NotificationApplication::class])
//@TestPropertySource(properties = ["fcm.enable=false"])
class NotificationApplicationTests {

    @Test
    fun contextLoads() {
    }

}
