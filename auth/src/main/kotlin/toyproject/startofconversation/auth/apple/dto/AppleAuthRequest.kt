package toyproject.startofconversation.auth.apple.dto

data class AppleAuthRequest(val identityToken: String, val user: AppleUser, val authorizationCode: String)

data class AppleUser(val name: AppleUserName, val email: String?, val sub: String)

data class AppleUserName(val firstName: String?, val lastName: String?)