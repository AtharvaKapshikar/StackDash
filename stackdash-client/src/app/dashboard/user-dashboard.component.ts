import { Component, OnInit } from '@angular/core';
import { ActivityComponent } from './activity/activity.component';
import { TasksComponent } from './tasks/tasks.component';
import { WelcomeComponent } from './welcome/welcome.component';
import { DashboardServiceService } from './dashboard-service.service';
import { LoaderComponent } from '../shared/loader/loader.component';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { DashboardDto } from './dashboard-dto';
import { Dashboarduserdto } from './dashboarduserdto';
import { PLATFORM_ID, Inject } from '@angular/core';
import { ProfileComponent } from "./profile/profile.component";
import { UserDashboardContentComponent } from "./user-dashboard-content/user-dashboard-content.component";
import { Router, RouterLink, RouterModule } from "@angular/router";
import { NotificationComponent } from './notification/notification.component';
import { jwtDecode } from 'jwt-decode';
import { getUserIdFromToken } from '../shared/auth-utils.ts';

@Component({
  selector: 'app-user-dashboard',
  standalone:true,
  imports: [ActivityComponent, TasksComponent, WelcomeComponent, LoaderComponent, CommonModule,RouterLink,
    UserDashboardContentComponent, ProfileComponent, UserDashboardContentComponent, NotificationComponent ,RouterModule],
  templateUrl: './user-dashboard.component.html',
  styleUrl: './user-dashboard.component.scss'
})
export class UserDashboardComponent implements OnInit {

  constructor(private service: DashboardServiceService,
    @Inject(PLATFORM_ID) private platformId: Object,
    private router:Router
  ){}
  
  userId: number | null = null; // just declare it
  loading = true;
  userData?: Dashboarduserdto;
  dashboardData!: DashboardDto;


ngOnInit() {
  if (isPlatformBrowser(this.platformId)) {
    // const rawId = localStorage.getItem("userId");
    // this.userId = rawId ? Number(rawId) : 0;

const token = localStorage.getItem('token');

if (token) {
  //const decoded: any = jwtDecode(token);

  // ✅ FIX: use userId directly
   this.userId = getUserIdFromToken(); // ✅ assign to class property
console.log('User ID from token:', this.userId);


  if (typeof this.userId !== 'number' || isNaN(this.userId) || this.userId <= 0) {
  console.warn('Invalid or missing userId in token');
  this.router.navigate(['/login']);
  return;
}

  console.log('User ID from token:', this.userId);
} else {
  console.warn('No token found in localStorage');
  this.router.navigate(['/login']);
  return;
}
// or decoded.userId if your token includes it  

    this.service.getDashboardData(this.userId).subscribe({
      next: (data) => {
        this.dashboardData = data;

        this.userData = {
          userId: data.userId,
          userName: data.userName,
          firstName: data.firstName,
          lastName: data.lastName,
          designation: data.designation,
          profilePicture: data.profilePicture,
          email: data.email,
          city: data.city,
          mobileNumber:data.mobileNumber,
          age:data.age,
          active: data.active,
          verified: data.verified,
          lastLogin: data.lastLogin,
          roles: data.roles
        };

        this.loading = false;
      },
      error: (err) => {
        console.error('Dashboard fetch failed', err);
        this.loading = false;
      }
    });
  }
}
}
