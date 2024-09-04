// TODO: src/configs/config.type.ts
export type AppConfig = {
    nodeEnv: string // 현재 실행 환경 (development, production, test 등)
    appName: string // 애플리케이션 이름
    baseDir: string // 애플리케이션의 기본 디렉토리 경로
    frontDomain: string // 프론트엔드 도메인 주소
    backDomain: string // 백엔드 도메인 주소
    port: number // 애플리케이션 포트 번호
    apiPrefix: string // API 엔드포인트의 접두사
    language: string // 기본 언어 설정
}

// TODO: 타입이 많아질 경우를 생각하여 구성
export type AppConfigType = {
    app: AppConfig
}