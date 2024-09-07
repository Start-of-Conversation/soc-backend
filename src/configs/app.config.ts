import { registerAs } from '@nestjs/config';
import { AppConfig } from './config.type';
import ValidateConfig from './validate.config';
import { IsEnum, IsInt, IsOptional, IsString, IsUrl, Max, Min } from 'class-validator';

// 실행 환경을 나타내는 Enum
enum Environment {
    dev = 'dev',
    prod = 'prod',
}

// 환경 변수 유효성을 검사하기 위한 클래스
class EnvironmentValidator {
    @IsEnum(Environment)
    @IsOptional()
    NODE_ENV?: Environment;

    @IsInt()
    @Min(0)
    @Max(65535)
    @IsOptional()
    APP_PORT?: number;

    @IsUrl({ require_tld: false })
    @IsOptional()
    FRONTEND_DOMAIN?: string;

    @IsUrl({ require_tld: false })
    @IsOptional()
    BACKEND_DOMAIN?: string;

    @IsString()
    @IsOptional()
    API_PREFIX?: string;

    @IsString()
    @IsOptional()
    LANGUAGE?: string;
}

// 설정 파일 등록 및 환경 변수 유효성 검사
export default registerAs<AppConfig>('app', () => {
    // 환경 변수 유효성을 검사하고 오류 발생 시 예외 처리
    ValidateConfig(process.env, EnvironmentValidator);

    // 설정 객체 반환
    return {
        nodeEnv: process.env.NODE_ENV || 'dev',
        appName: process.env.APP_NAME || 'app',
        baseDir: process.env.PWD || process.cwd(),
        frontDomain: process.env.FRONTEND_DOMAIN,
        backDomain: process.env.BACKEND_DOMAIN,
        port: process.env.APP_PORT
            ? parseInt(process.env.APP_PORT, 10)
            : process.env.PORT
                ? parseInt(process.env.PORT, 10)
                : 5000,
        apiPrefix: process.env.API_PREFIX || 'api',
        language: process.env.LANGUAGE || 'ko',
    };
});