import { RoleType } from "../dashboardmodel/enum/roletype";

export interface LoginResponse {
     userId: number;
  userName: string;
  token:string;
  roles:RoleType;
}
