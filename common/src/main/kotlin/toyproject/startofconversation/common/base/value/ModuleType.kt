package toyproject.startofconversation.common.base.value

/**
 * 오류를 발생시킨 주체를 저장하기 위한 enum입니다.
 */
enum class ModuleType {
    SYSTEM, NOTIFICATION, AUTH, API, PAYMENT;

    companion object {
        fun resolveFromURI(uri: String): ModuleType {
            return when {
                uri.startsWith("/auth") -> AUTH
                uri.startsWith("/notifications") -> NOTIFICATION
                uri.startsWith("/devices") -> NOTIFICATION
                uri.startsWith("/payment") -> PAYMENT
                uri.startsWith("/api") -> API
                else -> SYSTEM
            }
        }
    }
}