package toyproject.startofconversation.auth.apple.provider

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import toyproject.startofconversation.common.exception.SOCUnauthorizedException
import java.nio.charset.StandardCharsets
import java.security.PublicKey
import java.util.Base64


@Component
class AppleJwtProvider {

    companion object {
        private val OBJECT_MAPPER: ObjectMapper = ObjectMapper()
    }

    fun parseHeaders(token: String): Map<String, String> =
        runCatching {
            val header = token.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
            OBJECT_MAPPER.readValue(
                decodeHeader(header),
                object : TypeReference<Map<String, String>>() {}
            ) as Map<String, String>
        }.getOrNull() ?: throw SOCUnauthorizedException("Apple OAuth Identity Token 형식이 올바르지 않습니다.")

    fun decodeHeader(token: String?): String =
        String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8)

    fun getTokenClaims(token: String?, publicKey: PublicKey?): Claims =
        Jwts.parser().verifyWith(publicKey).build().parseSignedClaims(token).payload

}