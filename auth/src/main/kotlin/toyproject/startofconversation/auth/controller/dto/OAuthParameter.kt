package toyproject.startofconversation.auth.controller.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "소셜 로그인 요청에 필요한 파라미터 정보")
data class OAuthParameter(
    @Schema(description = "클라이언트 ID", example = "abc123clientid")
    val clientId: String,

    @Schema(description = "리디렉션 URI", example = "https://yourdomain.com/oauth/callback")
    val redirectUri: String,

    @Schema(description = "응답 타입", example = "code")
    val responseType: String,

    @Schema(description = "요청 scope (선택사항)", example = "name email", required = false)
    val scope: String? = null,

    @Schema(description = "응답 모드 (선택사항)", example = "form_post", required = false)
    val responseMode: String? = null,

    @Schema(description = "CSRF 방지를 위한 상태값 (선택사항)", example = "random-generated-state", required = false)
    val state: String? = null
)