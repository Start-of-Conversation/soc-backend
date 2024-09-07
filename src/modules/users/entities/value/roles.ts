export const roles = {
    ADMIN: { name: 'ADMIN', roleKr: '관리자' },
    PREMIUM: { name: 'PREMIUM', roleKr: '결제한 유저' },
    USER: { name: 'USER', roleKr: '기본 유저' },
    GUEST: { name: 'GUEST', roleKr: '게스트' },
} as const;

export type RoleName = keyof typeof roles;
export type Roles = typeof roles[RoleName];

export class RoleUtils {
    private static ALL_ROLES: Roles[] = Object.values(roles);

    static getRoleByName(name: string): Roles {
        if (name == null) {
            return roles.GUEST
        }
        const roleEntry = RoleUtils.ALL_ROLES.find(role => role.name === name);
        return roleEntry || roles.GUEST;
    }

    static getRoleByRoleKr(roleKr: string): Roles {
        if (roleKr == null) {
            roles.GUEST
        }
        const roleEntry = RoleUtils.ALL_ROLES.find(role => role.roleKr === roleKr);
        return roleEntry || roles.GUEST;
    }

    static getAllRoles(): typeof roles[keyof typeof roles][] {
        return RoleUtils.ALL_ROLES;
    }
}