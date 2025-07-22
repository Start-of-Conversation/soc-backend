package toyproject.startofconversation.notification.mail

import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import toyproject.startofconversation.common.base.dto.ResponseData

@Profile("dev")
@RestController
@RequestMapping("/api/test/mail")
class MailTestController(
    private val mailService: MailService
) {

    @PostMapping("/text")
    fun sendTextMail(
        @RequestBody request: MailTestRequest
    ): ResponseData<Boolean> = with(request) {
        mailService.sendMail(to, subject, body)
        ResponseData.to("텍스트 메일 전송 완료: $to", true)
    }

    @PostMapping("/html")
    fun sendHtmlMail(
        @RequestBody request: MailTestRequest
    ): ResponseData<Boolean> = with(request) {
        mailService.sendHtmlMail(to, subject, body)
        ResponseData.to("HTML 메일 전송 완료: $to", true)
    }

    data class MailTestRequest(
        val to: String,
        val subject: String,
        val body: String
    )
}