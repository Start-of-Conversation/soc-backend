package toyproject.startofconversation.notification.fcm.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import toyproject.startofconversation.common.exception.SOCException
import toyproject.startofconversation.notification.fcm.config.properties.FCMProperties

@Configuration
class FCMConfig(
    private val fcmProperties: FCMProperties
) {

    @Bean
    fun firebaseApp(): FirebaseApp {
        if (!FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.getInstance()
        }

        val resource = ClassPathResource(fcmProperties.serviceAccountFile)
        if (!resource.exists()) throw SOCException("FCM 키 파일이 존재하지 않습니다.")

        val options = resource.inputStream.use { inputStream ->
            FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(inputStream))
                .build()
        }
        return FirebaseApp.initializeApp(options)
    }
}