package toyproject.startofconversation.auth.domain.policy

object PasswordPolicy {
    const val REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&#])[A-Za-z\\d@\$!%*?&#]{8,}$"
    const val MESSAGE = "비밀번호 형식이 올바르지 않습니다. 8자 이상, 대소문자, 숫자, 특수문자 포함"
}