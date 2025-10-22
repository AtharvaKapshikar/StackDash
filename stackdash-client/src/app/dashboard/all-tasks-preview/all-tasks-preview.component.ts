import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { AdminDashboardServiceService } from '../admin-dashboard/admin-dashboard-service.service';
import { Task } from '../../dashboardmodel/task';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-all-tasks-preview',
  standalone:true,
  imports: [CommonModule, RouterLink],
  templateUrl: './all-tasks-preview.component.html',
  styleUrl: './all-tasks-preview.component.scss'
})
export class AllTasksPreviewComponent implements OnInit{


  recenttasks : Task[]=[];
  errorMsg="";
    isLoading = false;
   searchTerm = '';
   message: string | null = null;
messageType: 'success' | 'error' = 'success';
  totalPages = 0;
  pageSize = 10;
  currentPage = 0;
  filteredTasks: Task[] = [];

  constructor(private service:AdminDashboardServiceService){}


   ngOnInit() {
    this.loadTasks(this.currentPage);
  }


  loadTasks(page: number = 0) {
  this.isLoading = true;

  this.service.getAllTasks(page, this.pageSize).subscribe({
    next: (response: any) => {
      const parsed = typeof response === 'string' ? JSON.parse(response) : response;

      this.recenttasks = (parsed.content ?? []).slice(0,5);
      this.totalPages = parsed.totalPages ?? 1;
      this.currentPage = parsed.number ?? page;
      this.isLoading = false;

      console.log("✅ Tasks loaded:", this.recenttasks);
      console.log("📄 Total pages:", this.totalPages);
    },
    error: (error) => {
      this.errorMsg = error.message || 'Failed to load tasks';
      this.isLoading = false;
    }
  });
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
