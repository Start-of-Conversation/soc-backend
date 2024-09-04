export const loginChannel = {
    LOCAL: { name: 'LOCAL', role: '로컬' },
    KAKAO: { name: 'KAKAO', role: '카카오 소셜 로그인' },
    NAVER: { name: 'NAVER', role: '네이버 소셜 로그인' },
} as const;

export type LoginChannel = typeof loginChannel[keyof typeof loginChannel]['name'];