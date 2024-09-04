import * as argon2 from "argon2";
import { BeforeInsert, Column, Entity } from "typeorm";
import { Roles, roles } from "./value/roles";
import { BaseEntity } from "src/modules/common/base.entity";
import { Domain } from "src/modules/common/value/domain";
import { LoginChannel, loginChannel } from "./value/loginChannel";


@Entity("users")
export class Users extends BaseEntity {

    @Column({ unique: true })
    email: string;

    @Column('varchar')
    password: string;

    @Column('varchar')
    nickname: string;

    @Column({ default: "" })
    profileImage: string;

    @Column({ default: roles.USER })
    role: Roles;

    @Column({ default: loginChannel.LOCAL })
    loginChannel: LoginChannel;

    @BeforeInsert()
    async hashPassword() {
        this.password = await argon2.hash(this.password);
    }

    constructor(
        email: string,
        password: string,
        nickname: string,
        profileImage: string,
        role: Roles,
        loginChannel: LoginChannel) {
        super(Domain.USER);
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.role = role;
        this.loginChannel = loginChannel;
    }
}