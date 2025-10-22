import { Component, OnInit } from '@angular/core';
import { AdminDashboardServiceService } from '../admin-dashboard/admin-dashboard-service.service';
import { Task } from '../../dashboardmodel/task';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink, RouterOutlet, Router, Route, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-edit-task',
  standalone:true,
  imports: [CommonModule,FormsModule, RouterOutlet,RouterLink],
  templateUrl: './edit-task.component.html',
  styleUrl: './edit-task.component.scss'
})
export class EditTaskComponent {

  

  message: string | null = null;
messageType: 'success' | 'error' = 'success';

  constructor(private service:AdminDashboardServiceService, private router: Router, private route:ActivatedRoute){}

 task: Task = {} as Task; // always defined from the start


  ngOnInit() {
   const id = this.route.snapshot.paramMap.get('id');
   console.log("task id: ", id)
    const taskid = id;
  if (id) {
    this.service.getTask(+id).subscribe(task => {
      this.task = task;
     //  console.log("task : ", task)
    },);
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
      this.message = 'Unable to update task, please try later.';
      this.messageType = 'error';
    }
  });
}
  
}
