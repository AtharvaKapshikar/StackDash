import { CommonModule, isPlatformBrowser } from '@angular/common';
import { Component, Inject, PLATFORM_ID } from '@angular/core';
import { Route, Router, RouterLink } from '@angular/router';
import { Task } from '../../dashboardmodel/task';
import { AdminDashboardServiceService } from '../admin-dashboard/admin-dashboard-service.service';
import { jwtDecode } from 'jwt-decode';
import { getUserIdFromToken } from '../../shared/auth-utils.ts';
import { DashboardServiceService } from '../dashboard-service.service';

@Component({
  selector: 'app-user-tasks-preview',
  standalone:true,
  imports: [CommonModule, RouterLink],
  templateUrl: './user-tasks-preview.component.html',
  styleUrl: './user-tasks-preview.component.scss'
})
export class UserTasksPreviewComponent {

   recenttasks : Task[] = [];
    errorMsg="";
  
    constructor(private service:DashboardServiceService,@Inject(PLATFORM_ID) private platformId: Object,
  private router: Router){}
  
ngOnInit(): void {
   if (isPlatformBrowser(this.platformId)) {
   
const token = localStorage.getItem('token');

    const userId = getUserIdFromToken();
      console.log('User ID from token:', userId);

if (token) {

  if (userId !== null && !isNaN(userId)) {
  this.service.getUserTasks(userId).subscribe({
    next: (tasks) => {
      console.log("task:", tasks);
      if (tasks.length === 0) {
        this.recenttasks = [];
        this.errorMsg = "No tasks assigned to you."; // ✅ fallback message
      } else {
        this.recenttasks = tasks.slice(0, 3); // Show top 3
        this.errorMsg = ""; // Clear any previous error
      }
    },
    error: (error) => {
      this.errorMsg = error.error || "Failed to fetch tasks.";
    }
  });
} else {
  console.warn('No token found in localStorage');
  this.errorMsg = "Login required to view tasks.";
}
}  
  }
}

editTask(task:Task){
    console.log("from edit task method:", task.id);
    this.router.navigate([`user/tasks/edit/${task.id}`]);
   }
  
  
    getStatusClass(status: string): string {
     // console.log("status:", status);
    switch (status) {
      case 'Completed': return 'status-completed';
      case 'Pending': return 'status-pending';
      case 'In Progress': return 'status-in-progress';
      default: return 'Invalid';
    }
  }
}
