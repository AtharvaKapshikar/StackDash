export interface CustomJwtPayload {
    sub: string;
  userId: number;
  roles: string[];
  iat?: number;
  exp?: number;

}


