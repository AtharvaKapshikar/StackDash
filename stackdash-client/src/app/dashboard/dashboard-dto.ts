import { Dashboarduserdto } from "./dashboarduserdto";

export interface DashboardDto {
  userId: number;
  userName: string;
  firstName: string;
  lastName: string;
  designation: string;
  profilePicture: string;
  email: string;
  city: string;
  mobileNumber:string;
  age:number;
  active: boolean;
  verified: boolean;
  lastLogin: string;
  roles: string[];
  // other dashboard fields...
}
