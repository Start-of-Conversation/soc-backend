import * as argon2 from "argon2";
import { BeforeInsert, Column, Entity } from "typeorm";
import { Roles, roles } from "./value/roles";

@Entity("users")
export class Users {

    @Column({ unique: true })
    email: string;

    @Column()
    password: string;

    @Column()
    nickname: string;

    @Column({ default: "" })
    profileImage: string;

    @Column({ default: roles.USER })
    role: Roles;

    @Column({ default: "" })
    loginChannel: string;

    @BeforeInsert()
    async hashPassword() {
        this.password = await argon2.hash(this.password);
    }
}