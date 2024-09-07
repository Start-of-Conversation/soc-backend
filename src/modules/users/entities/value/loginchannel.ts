export const loginChannel = {
    LOCAL: { name: 'LOCAL', nameKr: '로컬' },
    KAKAO: { name: 'KAKAO', nameKr: '카카오 소셜 로그인' },
    NAVER: { name: 'NAVER', nameKr: '네이버 소셜 로그인' },
} as const;

export type ChannelName = keyof typeof loginChannel;
export type LoginChannel = typeof loginChannel[ChannelName];

export class ChannelUtil {

    private static ALL_CHANNELS: LoginChannel[] = Object.values(loginChannel);

    static getChannelByName(name: string): LoginChannel {
        if (name == null) {
            return loginChannel.LOCAL
        }
        const channelEntry = ChannelUtil.ALL_CHANNELS.find(channel => channel.name === name);
        return channelEntry || loginChannel.LOCAL;
    }

    static getRoleByRoleKr(nameKr: string): LoginChannel {
        if (nameKr == null) {
            return loginChannel.LOCAL
        }
        const channelEntry = ChannelUtil.ALL_CHANNELS.find(loginChannel => loginChannel.nameKr === nameKr);
        return channelEntry || loginChannel.LOCAL;
    }

    static getAllRoles(): typeof loginChannel[keyof typeof loginChannel][] {
        return ChannelUtil.ALL_CHANNELS;
    }
}