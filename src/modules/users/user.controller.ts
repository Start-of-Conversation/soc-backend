import { Controller, Get, Post } from '@nestjs/common';
import { UserService } from 'src/modules/users/user.service';

@Controller()
export class UserController {
    constructor(private readonly userService: UserService) { }

    @Post()
    registerUser() {
        return this.userService.saveUser();
    }
}