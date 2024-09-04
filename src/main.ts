
import { NestFactory } from '@nestjs/core'
import { ConfigService } from '@nestjs/config'
import { AppConfigType } from './configs/config.type'
import { AppModule } from './app.module'
import process from 'process'

async function bootstrap() {
	const app = await NestFactory.create(AppModule, { cors: true })

	// TODO: ConfigService를 통해 설정 값을 가져옴
	const configService = app.get(ConfigService<AppConfigType>)

	// TODO: 환경변수 API 전역 접두사 설정
	// TODO: localhost:5000/api/ 설정
	app.setGlobalPrefix(
		configService.getOrThrow('app.apiPrefix', { infer: true }),
		{
			exclude: ['/']
		}
	)
	// TODO: 환경변수 포트 설정
	const port = configService.getOrThrow('app.port', { infer: true })
	await app.listen(port)
	console.log(`listening on port ${port}`)

}
void bootstrap()