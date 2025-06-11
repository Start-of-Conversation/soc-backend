package toyproject.startofconversation.auth.domain.entity.value

import toyproject.startofconversation.common.exception.SOCNotFoundException

enum class AuthProvider(val value: String) {
    APPLE("애플"), KAKAO("카카오"), NAVER("네이버"), LOCAL("로컬");

    companion object {
        fun from(value: String): AuthProvider =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) }
                ?: throw SOCNotFoundException("Unknown AuthProvider: $value")
    }
}