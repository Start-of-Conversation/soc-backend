import * as argon2 from "argon2";
import { BeforeInsert, Column, Entity } from "typeorm";
import { RoleName, roles } from "./value/roles";
import { BaseEntity } from "src/modules/common/base.entity";
import { Domain } from "src/modules/common/value/domain";
import { ChannelName, loginChannel } from "./value/loginChannel";
import { UserSaveDto } from "../dto/users.register.request";
import { IsEmail } from "class-validator";
import { UsersBuilder } from "./users.builder";

@Entity("users")
export class Users extends BaseEntity {

    @IsEmail()
    @Column({ unique: true })
    private _email: string;

    @Column('varchar')
    private _password: string;

    @Column('varchar')
    private _nickname: string;

    @Column({ default: "" })
    private _profileImage: string;

    @Column({ default: roles.USER })
    private _role: RoleName;

    @Column({ default: loginChannel.LOCAL })
    private _loginChannel: ChannelName;

    @BeforeInsert()
    async hashPassword() {
        this._password = await argon2.hash(this._password);
    }

    constructor(
        email: string,
        password: string,
        nickname: string,
        profileImage: string,
        role: RoleName,
        loginChannel: ChannelName) {
        super(Domain.USER);
        this._email = email;
        this._password = password;
        this._nickname = nickname;
        this._profileImage = profileImage;
        this._role = role;
        this._loginChannel = loginChannel;
    }

    public static builder(): UsersBuilder {
        return new UsersBuilder();
    }

    public static of(request: UserSaveDto): Users {
        return Users.builder()
            .email(request.email)
            .password(request.password)
            .nickname(request.nickname)
            .profileImage(request.profileImage)
            .role(request.role)
            .loginChannel(request.loginChannel)
            .build();
    }
}