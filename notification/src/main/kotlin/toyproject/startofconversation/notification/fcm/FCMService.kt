package toyproject.startofconversation.notification.fcm

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import org.springframework.stereotype.Service
import toyproject.startofconversation.common.exception.external.FirebaseException
import toyproject.startofconversation.common.logger.logger
import toyproject.startofconversation.notification.domain.repository.DeviceRepository
import toyproject.startofconversation.notification.fcm.config.properties.FCMProperties

@Service
class FCMService(
    private val deviceRepository: DeviceRepository,
    private val fcmProperties: FCMProperties
) {
    private val log = logger()

    fun subscribeMarketing(userId: String) {
        val tokens = getDeviceToken(userId)
        if (tokens.isEmpty()) {
            log.warn("⚠️ 유저 [$userId]에 대한 유효한 deviceToken이 없습니다.")
            return
        }

        subscribeToTopic(tokens, fcmProperties.topicName)
    }

    fun unsubscribeMarketing(userId: String) {
        val tokens = getDeviceToken(userId)
        if (tokens.isEmpty()) {
            log.warn("⚠️ 유저 [$userId]에 대한 유효한 deviceToken이 없습니다.")
            return
        }

        unsubscribeFromTopic(tokens, fcmProperties.topicName)
    }

    fun subscribeToTopic(token: String, topic: String) = subscribeToTopic(listOf(token), topic)

    fun subscribeToTopic(tokens: List<String>, topic: String) {
        try {
            FirebaseMessaging.getInstance().subscribeToTopic(tokens, topic)
            log.info("✅ [$tokens] subscribed to [$topic]")
        } catch (e: FirebaseMessagingException) {
            throw FirebaseException("FCM 토픽 구독 실패: ${e.message}", e)
        }
    }

    fun unsubscribeFromTopic(token: String, topic: String) = unsubscribeFromTopic(listOf(token), topic)

    fun unsubscribeFromTopic(tokens: List<String>, topic: String) {
        try {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(tokens, topic)
            log.info("✅ [$tokens] unsubscribed from [$topic]")
        } catch (e: FirebaseMessagingException) {
            throw FirebaseException("FCM 토픽 구독 해제 실패: ${e.message}", e)
        }
    }

    private fun getDeviceToken(userId: String): List<String> = deviceRepository.findAllByUserId(userId)
        .filter { it.isPushEnabled && it.isTokenValid }
        .map { it.deviceToken }
}