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
class AppleSocialLoginTest {

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
    fun `애플 소셜 로그인 성공`() {
        val mockAuth = Auth(
            user = Users(nickname = "AppleUser"),
            email = "apple@example.com",
            authProvider = AuthProvider.APPLE,
            authId = "appleId"
        )

        whenever(authService.loginUser(any(), any(), any(), eq(AuthProvider.APPLE)))
            .thenReturn(ResponseEntity.ok(ResponseData.to(AuthResponse.from(mockAuth))))

        mockMvc.perform(
            post("/auth/apple")
                .param("code", "dummy_code")
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data.email").value("apple@example.com"))
            .andExpect(jsonPath("$.data.nickname").value("AppleUser"))
    }
}