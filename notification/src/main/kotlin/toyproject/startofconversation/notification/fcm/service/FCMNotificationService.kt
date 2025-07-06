package toyproject.startofconversation.notification.fcm.service

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.MulticastMessage
import com.google.firebase.messaging.Notification
import org.springframework.stereotype.Service
import toyproject.startofconversation.common.exception.external.FirebaseException
import toyproject.startofconversation.common.logger.logger

@Service
class FCMNotificationService {
    private val log = logger()

    fun sendMessageToDevice(
        token: String,
        title: String,
        body: String,
        data: Map<String, String>? = null
    ) = sendWithCatch {
        val messageBuilder = Message.builder()
            .setToken(token)
            .setNotification(buildNotification(title, body))

        data?.let {
            messageBuilder.putAllData(it)
        }

        val message = messageBuilder.build()
        val response = FirebaseMessaging.getInstance().send(message)
        log.info("FCM 메시지 전송 성공: {}", response)
    }

    fun sendMessagesToDevices(
        tokens: List<String>,
        title: String,
        body: String,
        data: Map<String, String> = emptyMap()
    ) = sendWithCatch {
        if (tokens.isEmpty()) {
            log.warn("token list is empty")
            return@sendWithCatch
        }

        val message = MulticastMessage.builder()
            .putAllData(data)
            .setNotification(buildNotification(title, body))
            .addAllTokens(tokens)
            .build()

        val response = FirebaseMessaging.getInstance().sendMulticast(message)
        log.info("FCM 전송 완료: 성공 {}, 실패 {}", response.successCount, response.failureCount)
        response.responses.forEachIndexed { index, sendResponse ->
            if (!sendResponse.isSuccessful) {
                log.warn("실패한 토큰 [{}]: {}", tokens[index], sendResponse.exception?.message)
            }
        }
    }

    private fun buildNotification(title: String, body: String): Notification =
        Notification.builder()
            .setTitle(title)
            .setBody(body)
            .build()

    private inline fun sendWithCatch(block: () -> Unit) {
        try {
            block()
        } catch (e: FirebaseMessagingException) {
            log.error("FCM 메시지 전송 중 예외 발생", e)
            throw FirebaseException("FCM 메시지 전송 중 오류 발생", e)
        }
    }

}