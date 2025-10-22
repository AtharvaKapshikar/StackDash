import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AdminDashboardServiceService } from '../admin-dashboard/admin-dashboard-service.service';
import { CommonModule } from '@angular/common';
import { Task } from '../../dashboardmodel/task';
import { FormsModule, NgModel } from '@angular/forms';
import { RouterLink, RouterOutlet, Router } from '@angular/router';


@Component({
  selector: 'app-tasks',
  standalone:true,
  imports: [CommonModule, FormsModule, RouterLink,RouterOutlet],
  templateUrl: './tasks.component.html',
  styleUrl: './tasks.component.scss'
})
export class TasksComponent implements OnInit {

    tasks : Task[]=[];
    isLoading = false;
    errorMsg="";
   searchTerm = '';
   message: string | null = null;
messageType: 'success' | 'error' = 'success';
  totalPages = 0;
  pageSize = 10;
  currentPage = 0;
  filteredTasks: Task[] = [];
  
  

  constructor(private service: AdminDashboardServiceService, private router: Router){}


  ngOnInit() {
    this.loadTasks(this.currentPage);
  }

loadTasks(page: number = 0) {
  if (typeof window === 'undefined') {
    console.warn('SSR: Skipping task load on server.');
    return;
  }

  const token = localStorage.getItem('token');
  if (!token) {
    this.message = 'Please login first.';
    this.messageType = 'error';
    this.router.navigate(['/login']);
    return;
  }

  this.isLoading = true;

  this.service.getAllTasks(page, this.pageSize).subscribe({
    next: (response: any) => {
      let parsedResponse: any;

      try {
        parsedResponse = typeof response === 'string' ? JSON.parse(response) : response;
      } catch (err) {
        console.error('❌ Failed to parse tasks JSON:', err);
        this.tasks = [];
        this.filteredTasks = [];
        this.isLoading = false;
        return;
      }

      this.tasks = Array.isArray(parsedResponse) ? parsedResponse : parsedResponse.content ?? [];
      this.filteredTasks = [...this.tasks];
      this.totalPages = parsedResponse.totalPages ?? 1;
      this.currentPage = parsedResponse.number ?? 0;
      this.isLoading = false;

      console.log("✅ Parsed tasks:", this.tasks);
    },
    error: (error) => {
      this.errorMsg = error.message || 'Failed to load tasks';
      this.isLoading = false;
      console.error("❌ Error fetching tasks:", error);
    }
  });
}
//     loadTasks(page: number = 0) {
//   this.isLoading = true;
//   this.service.getAllTasks(page, this.pageSize).subscribe({
//     next: (response) => {
//       console.log('Full API response:', response);
//         this.tasks = response;
//         this.filteredTasks = [...this.tasks];

//       this.totalPages = 1;
//       this.currentPage = page;
//       this.isLoading = false;
//       console.log('Fetched tasks:', this.tasks);
//     },
//     error: (err) => {
//       this.isLoading = false;
//         this.errorMsg = err.message || 'Failed to load tasks';
//       }
//   });
// }




   editTask(task:Task){
    console.log("from edit task method:", task.id);
    this.router.navigate([`admin/tasks/edit/${task.id}`]);
   }
  
    deleteTask(id: number) {
      const confirmed = confirm('Are you sure you want to delete this task?');
      if (confirmed) {
        this.service.deleteTask(id).subscribe({
        next: (response) =>{
         this.message = '✅ Task deleted successfully!';
         this.messageType = 'success';
        },
      error : (err) => {
        console.error("Error occured while deleteing task", err);
        this.message = err.error;
        this.messageType = 'error';
      }
    })
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

}
