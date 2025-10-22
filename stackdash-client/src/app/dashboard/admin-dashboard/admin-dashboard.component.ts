import { Component, Inject, OnInit, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { WelcomeComponent } from '../welcome/welcome.component';
import { ProfileComponent } from '../profile/profile.component';
import { TasksComponent } from '../tasks/tasks.component';
import { ActivityComponent } from '../activity/activity.component';
import { GraphComponent } from "../graph-component/graph-component.component";
import { AdminDashboardServiceService } from './admin-dashboard-service.service';
import { Router, RouterLink } from '@angular/router';
import { AllUsersComponentComponent } from "../all-users-component/all-users-component.component";
import { AllUsersPreviewComponent } from "../all-users-preview/all-users-preview.component";
import { AllTasksPreviewComponent } from "../all-tasks-preview/all-tasks-preview.component";


@Component({
  selector: 'app-admin-dashboard',
  standalone:true,
  imports: [CommonModule, TasksComponent, GraphComponent, RouterLink, AllUsersComponentComponent, AllUsersPreviewComponent, AllTasksPreviewComponent],
  templateUrl: './admin-dashboard.component.html',
  styleUrl: './admin-dashboard.component.scss'
})
export class AdminDashboardComponent implements OnInit {

  constructor(private service: AdminDashboardServiceService, private router: Router
    //@Inject(PLATFORM_ID) private platformId: Object
  ){}
  
  userId!: number; // just declare it
  //loading = true;
  taskCounts: { key: string; value: number } | null = null;
userCount: { userCount: number; activeUser: number } = {
  userCount: 0,
  activeUser: 0
};
  errorMsg: any;


ngOnInit() {
    this.service.getTaskCount().subscribe({
      next: (data: { [key: string]: number }) => {
      
        const keys = Object.keys(data);
        const values = Object.values(data);

        if (keys.length > 0) {
          this.taskCounts = {
            key: keys[0],
            value: values[0]
         };
       }
      },
      error: (err) => {
        console.log("error occured while calling service method..");
        this.errorMsg = err.errorMessage;
      }
})

  this.service.getDashboardUserCount().subscribe({
    next: (userCount: { [key: string]: number }) => {
   // console.log("user count:", userCount);

    this.userCount = {
      userCount: userCount["User Count"],
      activeUser: userCount["Active User"]
    };
  }, error: (err) => {
        console.log("error occured while calling user count service method..");
        this.errorMsg = err.errorMessage;
      }
  })

}
}
