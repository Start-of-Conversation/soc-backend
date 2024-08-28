import { Module } from '@nestjs/common';
import { AppController } from './app.controller';
import { AppService } from './app.service';
import { ConfigModule } from "@nestjs/config";
import { TypeOrmModule } from '@nestjs/typeorm';
import { UsersModule } from './modules/users/users.module';
import configuration from "./config/configuration";

@Module({
	imports: [
		TypeOrmModule.forRoot(),
		ConfigModule.forRoot({
			cache: true,
			isGlobal: true,
			load: [configuration],
		}),
		UsersModule],
	controllers: [AppController],
	providers: [AppService],
})
export class AppModule { }
