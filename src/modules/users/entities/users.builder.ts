import { Users } from "./users.entity";
import { ChannelName, ChannelUtil, LoginChannel } from "./value/loginChannel";
import { RoleName, RoleUtils, Roles, roles } from "./value/roles";

export class UsersBuilder {

    private _email: string;
    private _password: string;
    private _nickname: string;
    private _profileImage: string;
    private _role: RoleName;
    private _loginChannel: ChannelName;

    email(email: string) {
        this._email = email;
        return this;
    }

    password(password: string) {
        this._password = password;
        return this;
    }

    nickname(nickname: string) {
        this._nickname = nickname;
        return this;
    }

    profileImage(profileImage: string) {
        this._profileImage = profileImage;
        return this;
    }

    role(role: string) {
        this._role = RoleUtils.getRoleByName(role).name;
        return this;
    }

    loginChannel(loginChannel: string) {
        this._loginChannel = ChannelUtil.getChannelByName(loginChannel).name;
        return this;
    }

    build(): Users {
        return new Users(
            this._email,
            this._password,
            this._nickname,
            this._profileImage,
            this._role,
            this._loginChannel
        )
    }
}