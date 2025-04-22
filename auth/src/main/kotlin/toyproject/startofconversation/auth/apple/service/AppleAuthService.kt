package toyproject.startofconversation.auth.apple.service

import jakarta.transaction.Transactional
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import toyproject.startofconversation.auth.apple.AppleAuthClient
import toyproject.startofconversation.auth.apple.ApplePublicKeyGenerator
import toyproject.startofconversation.auth.apple.dto.AppleAuthRequest
import toyproject.startofconversation.auth.apple.provider.AppleJwtProvider
import toyproject.startofconversation.auth.domain.entity.Auth
import toyproject.startofconversation.auth.domain.entity.value.AuthProvider
import toyproject.startofconversation.auth.domain.repository.AuthRepository
import toyproject.startofconversation.auth.util.RandomNameMaker
import toyproject.startofconversation.common.domain.user.entity.Users
import toyproject.startofconversation.common.domain.user.repository.UsersRepository
import java.security.PublicKey
import javax.security.sasl.AuthenticationException

@Service
@RequiredArgsConstructor
class AppleAuthService(
    private val appleAuthClient: AppleAuthClient,
    private val applePublicKeyGenerator: ApplePublicKeyGenerator,
    private val appleJwtProvider: AppleJwtProvider,
    private val authRepository: AuthRepository,
    private val usersRepository: UsersRepository
) {
    @Transactional
    @Throws(AuthenticationException::class)
    fun loadUser(appleAuthRequest: AppleAuthRequest): Auth {
        //accountId 가져오기
        val accountID: String = getAppleAccountId(appleAuthRequest.identityToken)
        authRepository.findByAuthId(accountID)?.let { return it }

        //이름 생성
        var name = (appleAuthRequest.user.name.lastName ?: "") + " " + (appleAuthRequest.user.name.firstName ?: "")
        if (name.isBlank()) {
            name = RandomNameMaker.generate()
        }
        //이메일 처리
        val email = appleAuthRequest.user.email ?: throw AuthenticationException("Email is required")

        //user 저장
        val user = usersRepository.findByEmail(email) ?: usersRepository.save(
            Users.to(email = email, nickname = name)
        )

        return authRepository.save(
            Auth(
                user = user,
                authProvider = AuthProvider.APPLE,
                authId = appleAuthRequest.user.sub
            )
        )
    }

    private fun getAppleAccountId(identityToken: String): String {
        val headers: Map<String, String> = appleJwtProvider.parseHeaders(identityToken)
        //Feign Client를 사용하여 공개키 요청
        val publicKey: PublicKey =
            applePublicKeyGenerator.generatePublicKey(headers, appleAuthClient.getAppleAuthPublicKey())
        return appleJwtProvider.getTokenClaims(identityToken, publicKey).subject
    }

}