import { CommonModule, isPlatformBrowser } from '@angular/common';
import { Component, Inject, PLATFORM_ID } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { AdminDashboardServiceService } from '../admin-dashboard/admin-dashboard-service.service';
import { Task } from '../../dashboardmodel/task';
import { FormsModule } from '@angular/forms';
import { jwtDecode } from 'jwt-decode';
import { getUserIdFromToken } from '../../shared/auth-utils.ts';
import { DashboardServiceService } from '../dashboard-service.service';

@Component({
  selector: 'app-user-all-task',
  standalone:true,
  imports: [CommonModule,  RouterOutlet, FormsModule],
  templateUrl: './user-all-task.component.html',
  styleUrl: './user-all-task.component.scss'
})
export class UserAllTaskComponent {


   tasks : Task[]=[];
    errorMsg="";
   searchTerm = '';
   message: string | null = null;
messageType: 'success' | 'error' = 'success';
  
  filteredTasks: Task[] = [];
  
  

  constructor(private service: DashboardServiceService, private router: Router, @Inject(PLATFORM_ID) private platformId: Object){}

   ngOnInit(): void {
   if (isPlatformBrowser(this.platformId)) {
    // const userId = Number(localStorage.getItem('userId'));
    // console.log("user id from tasks:", userId);
    // // safe to use localStorage here
    const token = localStorage.getItem('token');
        const userId = getUserIdFromToken();
        console.log('User ID from token: activity', userId);

if (token) {
       
  console.log('User ID from token:', userId);
  if (userId !== null && !isNaN(userId)) {    
      this.service.getUserTasks(userId).subscribe(tasks => {
        this.tasks=tasks;
        console.log("Tasked feched" );
        this.filteredTasks = [...this.tasks];
      },
      error => {
        this.errorMsg = error.error;
      }
      );
    }
} else {
  console.warn('No token found in localStorage');
  // Optionally redirect to login or show error
}

  
  }
}

   filterTasks() {
  const term = this.searchTerm.trim().toLowerCase();
  this.filteredTasks = term
    ? this.tasks.filter(task => task.title.toLowerCase().includes(term) || 
                         task.id.toExponential().includes(term) ||
                        task.status.toLowerCase().includes(term))
    : [...this.tasks];
}

getStatusClass(status: string): string {
   // console.log("status:", status);
  switch (status) {
    
    case 'Completed': return 'status-completed';
    case 'Pending': return 'status-pending';
    case 'In Progress': return 'status-in-progress';
    default: return status;;
  }
}

editTask(task:Task){
    console.log("from edit task method:", task.id);
    this.router.navigate([`user/tasks/edit/${task.id}`]);
   }
}
