import { RoleDto } from "./roledto";

export interface User {
  userId: number;
  firstName:string;
  lastName:string;
  age:number;
  city:string;
  mobileNumber:string;
  email: string;      
  designation:string;
  active:boolean;
  verified:boolean;
  roles:RoleDto[];   
}
