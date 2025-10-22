import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink, RouterOutlet } from '@angular/router';
import { AdminDashboardServiceService } from '../admin-dashboard/admin-dashboard-service.service';
import { Task } from '../../dashboardmodel/task';
import { DashboardServiceService } from '../dashboard-service.service';

@Component({
  selector: 'app-update-user-task',
  standalone:true,
  imports: [CommonModule,FormsModule, RouterOutlet,RouterLink],
  templateUrl: './update-user-task.component.html',
  styleUrl: './update-user-task.component.scss'
})
export class UpdateUserTaskComponent {

  message: string | null = null;
messageType: 'success' | 'error' = 'success';

  constructor(private service:DashboardServiceService, private router: Router, private route:ActivatedRoute){}

 task: Task = {} as Task; // always defined from the start


  ngOnInit() {
   const id = this.route.snapshot.paramMap.get('id');
   console.log("task : ", id)
    const taskid = id;
  if (id) {
    this.service.getTask(+id).subscribe(task => {
      this.task = task;
       console.log("task : ", task)
    });
  }
}

  editTask() {
  if (!this.task) {
    console.error('Task is undefined. Cannot update.');
    return;
  }

  this.service.editTask(this.task).subscribe({
    next: (response) => {
      this.message = '✅ Task Updated successfully!';
      this.messageType = 'success';
      localStorage.setItem('userId', response.userId);
    },
    error: (err) => {
      console.error('Error occurred while updating task', err);
      this.message = err.error;
      this.messageType = 'error';
    }
  });
}
  
}
