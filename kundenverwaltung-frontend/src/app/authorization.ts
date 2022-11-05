export class JwtData {
    id: number | undefined;
    username: string | undefined;
    tenantId: string | undefined;
    refreshToken: string | undefined;
    token: string | undefined;
}

export class LoginData {
    username: string | undefined;
    tenantId: number | undefined;
    password: string | undefined;
}