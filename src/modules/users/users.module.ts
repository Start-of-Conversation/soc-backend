import { Module } from '@nestjs/common';
import { TypeOrmModule } from '@nestjs/typeorm';
import { Users } from './entities/users.entity';
import { UserController } from './user.controller';
import { UserService } from './user.service';
import { UsersRepository } from './repositories/user.repository';

@Module({
    imports: [TypeOrmModule.forFeature([Users])],
    controllers: [UserController],
    providers: [UserService, UsersRepository],
})
export class UsersModule { }
