export const roles = {
    ADMIN: { name: 'ADMIN', role: '관리자' },
    PREMIUM: { name: 'PREMIUM', role: '결제한 유저' },
    USER: { name: 'USER', role: '기본 유저' },
    GUEST: { name: 'GUEST', role: '게스트' },
} as const;

export type Roles = typeof roles[keyof typeof roles]['name'];