package toyproject.startofconversation.notification.fcm.service

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.MulticastMessage
import com.google.firebase.messaging.Notification
import org.springframework.stereotype.Service
import toyproject.startofconversation.common.base.value.ChannelType
import toyproject.startofconversation.common.domain.user.entity.Users
import toyproject.startofconversation.common.exception.external.FirebaseException
import toyproject.startofconversation.common.logger.logger
import toyproject.startofconversation.log.notification.publisher.NotificationLogPublisher
import toyproject.startofconversation.notification.device.domain.repository.DeviceRepository
import toyproject.startofconversation.notification.fcm.config.properties.FCMProperties

@Service
class FCMNotificationService(
    deviceRepository: DeviceRepository,
    private val fcmProperties: FCMProperties,
    private val logPublisher: NotificationLogPublisher
) : FCMBaseService(deviceRepository) {

    private val log = logger()

    fun sendMessageToUser(userId: String, title: String, body: String, data: Map<String, String> = emptyMap()) =
        logPublisher.withNotificationLog(userId, title, body, ChannelType.APP_PUSH) {
            sendMessagesToDevices(tokens = getDeviceToken(userId), title, body, data)
        }

    fun sendMessageToUser(user: Users, title: String, body: String, data: Map<String, String> = emptyMap()) =
        logPublisher.withNotificationLog(user, title, body, ChannelType.APP_PUSH) {
            sendMessagesToDevices(tokens = getDeviceToken(user), title, body, data)
        }

    /**
     * 기본 토픽으로 메세지 전송
     */
    fun sendMessageToSubscriber(
        title: String,
        body: String,
        data: Map<String, String> = emptyMap()
    ) = sendMessageToSubscriber(fcmProperties.topicName, title, body, data)

    /**
     * 구독자들에게 메세지 전송
     */
    fun sendMessageToSubscriber(
        topic: String,
        title: String,
        body: String,
        data: Map<String, String> = emptyMap()
    ) = sendWithCatch {
        val message = Message.builder()
            .setNotification(buildNotification(title, body))
            .setTopic(topic)
            .putAllData(data)
            .build()

        val response = FirebaseMessaging.getInstance().send(message)
        log.info("FCM 메시지 전송 성공: {}", response)
    }

    /**
     * 단일 메세지 전송
     */
    fun sendMessageToDevice(
        token: String,
        title: String,
        body: String,
        data: Map<String, String> = emptyMap()
    ) = sendWithCatch {
        val message = Message.builder()
            .setToken(token)
            .setNotification(buildNotification(title, body))
            .putAllData(data)
            .build()

        val response = FirebaseMessaging.getInstance().send(message)
        log.info("FCM 메시지 전송 성공: {}", response)
    }

    /**
     * 다중 메세지 전송
     */
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