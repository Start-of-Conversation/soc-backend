package toyproject.startofconversation.notification.fcm.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import toyproject.startofconversation.common.exception.SOCException
import toyproject.startofconversation.notification.fcm.config.properties.FCMProperties
import java.io.File

@Configuration
class FCMConfig(
    private val fcmProperties: FCMProperties
) {

    @Bean
    @ConditionalOnProperty(name = ["fcm.enable"], havingValue = "true")
    fun firebaseApp(): FirebaseApp {
        if (FirebaseApp.getApps().isNotEmpty()) {
            return FirebaseApp.getInstance()
        }

        val resource = getResource(fcmProperties.serviceAccountFile)
        if (!resource.exists()) throw SOCException("FCM 키 파일이 존재하지 않습니다.")

        val options = resource.inputStream.use {
            FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(it))
                .build()
        }
        return FirebaseApp.initializeApp(options)
    }

    private fun getResource(path: String): Resource {
        if (File(path).exists()) {
            return FileSystemResource(path)
        }
        return ClassPathResource(path)
    }
}