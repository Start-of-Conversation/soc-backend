package toyproject.startofconversation.common.base.value

enum class ChannelType(val description: String) {

    EMAIL("이메일"),
    APP_PUSH("앱 푸시"),
    WEBHOOK("이벤트가 생기면 다른 시스템에 자동으로 알림을 보냄");

    override fun toString() = description
}