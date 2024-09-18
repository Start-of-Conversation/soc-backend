import { Module } from '@nestjs/common'
import { ConfigModule } from '@nestjs/config'
import appConfig from './configs/app.config'
import { TypeOrmModule } from '@nestjs/typeorm'
import { DataSource, DataSourceOptions } from 'typeorm'
import { TypeormConfig } from './database/typeorm.config'
// import path from 'path' 
import * as path from 'path';
import { UsersModule } from './modules/users/users.module'
import { AppController } from './app.controller'
import { AppService } from './app.service'

@Module({
	imports: [
		ConfigModule.forRoot({
			isGlobal: true,
			load: [appConfig],
			envFilePath: path.resolve(__dirname, '..', 'src', 'env', `${process.env.NODE_ENV}` === 'prod' ? '.env.prod' : '.env.dev'),
		}),
		TypeOrmModule.forRootAsync({
			useClass: TypeormConfig,
		}),
		UsersModule,
		// 	// TODO: 추가
		// 	ConfigModule.forRoot({
		// 		isGlobal: true,
		// 		load: [appConfig],
		// 		envFilePath: path.resolve(
		// 			__dirname,
		// 			'..',
		// 			'src',
		// 			'env',
		// 			`${process.env.NODE_ENV}` === 'prod'
		// 				? '.env.prod'
		// 				: '.env.dev'
		// 		)
		// 	}),
		// 	TypeOrmModule.forRootAsync({
		// 		useClass: TypeormConfig,
		// 		dataSourceFactory: async (options: DataSourceOptions) => {
		// 			return new DataSource(options).initialize()
		// 		}
		// 	}),
		// 	UsersModule
	],
	controllers: [AppController],
	providers: [AppService]
})
export class AppModule { }