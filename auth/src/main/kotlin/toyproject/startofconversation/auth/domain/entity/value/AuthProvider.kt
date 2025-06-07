package toyproject.startofconversation.auth.domain.entity.value

import toyproject.startofconversation.common.exception.SOCNotFoundException

enum class AuthProvider {
    APPLE, KAKAO, NAVER, LOCAL;

    companion object {
        fun from(value: String): AuthProvider =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) }
                ?: throw SOCNotFoundException("Unknown AuthProvider: $value")
    }
}