




import { Module } from '@nestjs/common'
import { ConfigModule } from '@nestjs/config'
import appConfig from './configs/app.config'
import { TypeOrmModule } from '@nestjs/typeorm'
import { DataSource, DataSourceOptions } from 'typeorm'
import { TypeormConfig } from './database/typeorm.config'
import path from 'path'
import { UsersModule } from './modules/users/users.module'

@Module({
	imports: [
		// TODO: 추가
		ConfigModule.forRoot({
			isGlobal: true,
			load: [appConfig],
			envFilePath: path.resolve(   //TODO: src/env 폴더에 환경에 맞게 경로 설정
				__dirname,
				'..',
				'src',
				'env',
				`${process.env.NODE_ENV}` === 'production'
					? '.env.production'
					: '.env.development'
			)
		}),
		TypeOrmModule.forRootAsync({
			useClass: TypeormConfig,
			dataSourceFactory: async (options: DataSourceOptions) => {
				return new DataSource(options).initialize()
			}
		}),
		UsersModule
	]
})
export class AppModule { }
출처: https://stack94.tistory.com/entry/NestJS-nestconfig-사용하여-환경-변수-구성하자 [느린 개발자:티스토리]