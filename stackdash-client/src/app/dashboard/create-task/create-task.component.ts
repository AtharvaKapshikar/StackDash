import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { AdminDashboardServiceService } from '../admin-dashboard/admin-dashboard-service.service';
import { RouterLink, RouterOutlet } from '@angular/router';
import { Task } from '../../dashboardmodel/task';
import { FormsModule } from '@angular/forms';
import { AddTask } from '../../dashboardmodel/add-task';

@Component({
  selector: 'app-create-task',
  standalone:true,
  imports: [CommonModule, RouterLink,RouterOutlet, FormsModule,],
  templateUrl: './create-task.component.html',
  styleUrl: './create-task.component.scss'
})
export class CreateTaskComponent{

  newTask: AddTask = {
  title: '',
  description: '',
  status: '',
  dueDate: new Date,
  assignedById: 0,
  assignedToId: 0
  }

message: string | null = null;
messageType: 'success' | 'error' = 'success';
  currentUserId: any;



  constructor(private service: AdminDashboardServiceService){}

  
  addTask(){

    this.service.createTask(this.newTask).subscribe({
     next: (response) =>{
         this.message = '✅ Task created successfully!';
      this.messageType = 'success';
      localStorage.setItem('userId', response.userId);
      this.currentUserId = Number(localStorage.getItem('userId'));
      this.resetForm();
      this.clearMessageAfterDelay();
      },
      error : (err) => {
        console.error("Error occured while adding task", err);
      this.message = err.error;
      this.messageType = 'error';
      this.clearMessageAfterDelay();
      }
    })

  }

  resetForm() {
  this.newTask = {
    title: '',
    description: '',
    status: 'Pending',
    dueDate: new Date,
    assignedById: 0,
    assignedToId: 0
  };
}
  clearMessageAfterDelay() {
  setTimeout(() => this.message = null, 4000);
}

}
