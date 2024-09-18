import { TypeOrmModuleOptions, TypeOrmOptionsFactory } from '@nestjs/typeorm'
import { Injectable } from '@nestjs/common'
import { ConfigService } from '@nestjs/config';

@Injectable()
export class TypeormConfig implements TypeOrmOptionsFactory {
    constructor(private configService: ConfigService) { }

    createTypeOrmOptions(): TypeOrmModuleOptions {
        return {
            type: 'postgres',
            host: this.configService.get<string>('DB_HOST'),
            port: this.configService.get<number>('DB_PORT'),
            username: this.configService.get<string>('DB_USER'),
            password: this.configService.get<string>('DB_PASSWORD'),
            database: this.configService.get<string>('DB_NAME'),
            synchronize: this.configService.get<boolean>('DB_SYNCHRONIZE'),
            dropSchema: this.configService.get<boolean>('DB_DROP_SCHEMA'),
            logging: this.configService.get<boolean>('DB_LOGGING'),
            entities: [__dirname + '/../**/*.entity{.ts,.js}'],
            extra: {
                max: this.configService.get<number>('DB_MAX_CONNECTIONS'),
            },
        };
    }
}
// export class TypeormConfig implements TypeOrmOptionsFactory {
//     createTypeOrmOptions(): TypeOrmModuleOptions {
//         return {
//             type: 'postgres',  // TODO: 데이터베이스 종류 
//             host: 'localhost', // TODO: 데이터베이스 서버 호스트
//             port: 5432, // TODO: 데이터베이스 포트
//             username: 'postgres',
//             password: 'postgres',
//             database: 'start_of_conversation', // TODO: 연결할 데이터베이스 이름
//             synchronize: true,  // TODO: 스키마 자동 동기화 (production에서는 false)
//             dropSchema: false, // TODO: 애플리케이션 실행시 기존 스키마 삭제 여부
//             keepAlive: true, // TODO: 애플리케이션 재시작 시 연결 유지
//             logging: true, // TODO: 데이터베이스 쿼리 로깅 여부
//             entities: [__dirname + '/../**/*.entity{.ts,.js}'],  //TODO: 중요! 엔티티 클래스 경로
//             extra: {
//                 max: 100
//             }
//         } as TypeOrmModuleOptions
//     }
// }