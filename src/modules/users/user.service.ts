import { Injectable } from '@nestjs/common';
import { UsersRepository } from './repositories/user.repository';
import { UserSaveDto } from './dto/users.register.request';
import { Users } from './entities/users.entity';

@Injectable()
export class UserService {

    constructor(private readonly usersRepository: UsersRepository) { };

    async registerUser(request: UserSaveDto): Promise<Users> {
        const user = Users.of(request);
        await this.usersRepository.save(user);
        return await this.usersRepository.findByUserId(user.id);
    }
}
