import { Injectable } from '@nestjs/common';
import { ConfigService } from "@nestjs/config";

@Injectable()
export class AppService {
	constructor(private configService: ConfigService) { }

	getHello() {
		const host = this.configService.get<string>('app.host');
		const port = this.configService.get<number>('app.port', 3000);
		const dbHost = this.configService.get<string>('db.host');
		const dbPort = this.configService.get<number>('db.port', 5432);
		return {
			host,
			port,
			dbHost,
			dbPort,
		};
	}

	getSomeConfig() {
		const someConfig = this.configService.get<string>('SOME_CONFIG_KEY');
		if (!someConfig) {
			throw new Error('SOME_CONFIG_KEY is not defined in the environment variables');
		}
		return someConfig;
	}
}
