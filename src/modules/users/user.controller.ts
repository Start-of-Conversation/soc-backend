import { Body, Controller, Get, Post } from '@nestjs/common';
import { UserService } from 'src/modules/users/user.service';
import { UserSaveDto } from './dto/users.register.request';

@Controller('/api/user')
export class UserController {
    constructor(private readonly userService: UserService) { }

    @Post('/register')
    registerUser(@Body() requestDto: UserSaveDto) {
        return this.userService.registerUser(requestDto);
    }
}