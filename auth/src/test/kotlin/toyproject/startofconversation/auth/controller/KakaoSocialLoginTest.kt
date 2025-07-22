package toyproject.startofconversation.auth.controller

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever
import org.springframework.http.ResponseEntity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import toyproject.startofconversation.auth.controller.dto.AuthResponse
import toyproject.startofconversation.auth.domain.entity.Auth
import toyproject.startofconversation.auth.domain.entity.value.AuthProvider
import toyproject.startofconversation.auth.service.AuthService
import toyproject.startofconversation.auth.service.SocialLoginService
import toyproject.startofconversation.common.base.dto.ResponseData
import toyproject.startofconversation.common.domain.user.entity.Users
import kotlin.test.Test

@ExtendWith(MockitoExtension::class)
class KakaoSocialLoginTest {

    @InjectMocks
    private lateinit var authController: AuthController

    @Mock
    private lateinit var authService: AuthService

    @Mock
    private lateinit var socialLoginService: SocialLoginService

    private lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build()
    }

    @Test
    fun `카카오 소셜 로그인 성공`() {
        // given
        val authorizationCode = "dummy-auth-code"
        val mockAuth = Auth(
            user = Users(nickname = "테스트유저"),
            email = "test@example.com",
            authProvider = AuthProvider.KAKAO,
            authId = "provider-id"
        )

        whenever(authService.loginUser(any(), any(), any(), eq(AuthProvider.KAKAO)))
            .thenReturn(ResponseEntity.ok(ResponseData.to(AuthResponse.from(mockAuth))))

        // when + then
        mockMvc.perform(
            post("/auth/kakao")
                .param("code", authorizationCode)
        )
//            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data.email").value("test@example.com"))
    }

    @Test
    fun `카카오 소셜 로그인 실패 - code 없음`() {
        mockMvc.perform(post("/auth/kakao"))
            .andExpect(status().isBadRequest)
    }
}