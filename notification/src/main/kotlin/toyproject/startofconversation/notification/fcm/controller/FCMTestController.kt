package toyproject.startofconversation.notification.fcm.controller

import org.springframework.context.annotation.Profile
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import toyproject.startofconversation.notification.fcm.service.FCMNotificationService

@Profile("dev") //dev 프로파일이 활성화됐을 때만 빈 적용되도록 설정
@RestController
@RequestMapping("/api/fcm/test")
class FCMTestController(
    private val fcmNotificationService: FCMNotificationService
) {

    @PostMapping("/token")
    fun sendToToken(
        @RequestParam token: String,
        @RequestBody request: SendMessageRequest
    ): ResponseEntity<String> = with(request) {
        fcmNotificationService.sendMessageToDevice(token, title, body, data)
        return ResponseEntity.ok("메시지를 단일 디바이스로 전송했습니다.")
    }

    @PostMapping("/topic")
    fun sendToTopic(
        @RequestParam topic: String,
        @RequestBody request: SendMessageRequest
    ): ResponseEntity<String> = with(request) {
        fcmNotificationService.sendMessageToSubscriber(topic, title, body, data)
        return ResponseEntity.ok("메시지를 토픽 [$topic] 구독자에게 전송했습니다.")
    }

    data class SendMessageRequest(
        val title: String,
        val body: String,
        val data: Map<String, String> = emptyMap()
    )

}