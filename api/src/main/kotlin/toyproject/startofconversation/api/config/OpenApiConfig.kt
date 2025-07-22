package toyproject.startofconversation.api.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.context.annotation.Configuration

// http://localhost:8080/swagger-ui/index.html
@Configuration
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
@OpenAPIDefinition(
    info = Info(
        title = "SOC Api docs",
        version = "1.0"
    ),
    tags = [
        Tag(name = "Auth", description = "인증 관련 API"),
        Tag(name = "User", description = "사용자 관련 API"),
        Tag(name = "CardGroup", description = "카드그룹 관련 API"),
        Tag(name = "Card", description = "카드 관련 API"),
        Tag(name = "Device", description = "기기 관련 API"),
        Tag(name = "Admin", description = "관리자 관련 API"),
        Tag(name = "Likes", description = "좋아요 관련 API"),
        Tag(name = "Marketing", description = "좋아요 관련 API"),
    ]
)
class OpenApiConfig