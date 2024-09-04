




import { registerAs } from '@nestjs/config'
import { AppConfig } from './config.type'
import ValidateConfig from 'src/utils/validate.config'
import {
    IsEnum,
    IsInt,
    IsOptional,
    IsString,
    IsUrl,
    Max,
    Min
} from 'class-validator'
import process from 'process'

// 실행 환경을 나타내는 Enum
enum Environment {
    dev = 'development',
    prod = 'production',
}

// 환경 변수 유효성을 검사하기 위한 클래스
class EnvironmentValidator {
    @IsEnum(Environment)
    @IsOptional()
    NODE_ENV: Environment

    @IsInt()
    @Min(0)
    @Max(65535)
    @IsOptional()
    APP_PORT: number

    @IsUrl({ require_tld: false })
    @IsOptional()
    FRONTEND_DOMAIN: string

    @IsUrl({ require_tld: false })
    @IsOptional()
    BACKEND_DOMAIN: string

    @IsString()
    @IsOptional()
    API_PREFIX: string

    @IsString()
    @IsOptional()
    LANGUAGE: string
}

// 설정 파일 등록 및 환경 변수 유효성 검사
export default registerAs<AppConfig>('app', () => {
    // 환경 변수 유효성을 검사하고 오류 발생 시 예외 처리
    ValidateConfig(process.env, EnvironmentValidator)

    // 설정 객체 반환
    return {
        // NODE_ENV 환경 변수 값 또는 기본값으로 'development' 설정
        nodeEnv: process.env.NODE_ENV || 'development',

        // APP_NAME 환경 변수 값 또는 기본값으로 'app' 설정
        appName: process.env.APP_NAME || 'app',

        // PWD 환경 변수 값 또는 현재 작업 디렉토리로 설정
        baseDir: process.env.PWD || process.cwd(),

        // FRONT_DOMAIN 환경 변수 값 설정
        frontDomain: process.env.FRONT_DOMAIN,

        // BACK_DOMAIN 환경 변수 값 설정
        backDomain: process.env.BACK_DOMAIN,

        // APP_PORT 환경 변수 값 또는 PORT 환경 변수 값 또는 기본값으로 5000 설정
        port: process.env.APP_PORT
            ? parseInt(process.env.APP_PORT, 10)
            : process.env.PORT
                ? parseInt(process.env.PORT, 10)
                : 5000,

        // API_PREFIX 환경 변수 값 또는 기본값으로 'api' 설정
        apiPrefix: process.env.API_PREFIX || 'api',

        // LANGUAGE 환경 변수 값 또는 기본값으로 'ko' 설정 추후 i18n 설정을 위함
        language: process.env.LANGUAGE || 'ko',
    }
})
출처: https://stack94.tistory.com/entry/NestJS-nestconfig-사용하여-환경-변수-구성하자 [느린 개발자:티스토리]