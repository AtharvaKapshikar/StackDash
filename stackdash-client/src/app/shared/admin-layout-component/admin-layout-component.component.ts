import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { AdminDashboardComponent } from "../../dashboard/admin-dashboard/admin-dashboard.component";
import { TasksComponent } from "../../dashboard/tasks/tasks.component";
import { GraphComponent } from "../../dashboard/graph-component/graph-component.component";
import { AllUsersComponentComponent } from "../../dashboard/all-users-component/all-users-component.component";
import { CreateTaskComponent } from '../../dashboard/create-task/create-task.component';

@Component({
  selector: 'app-admin-layout-component',
  standalone:true,
  imports: [CommonModule, RouterOutlet, RouterLink, AdminDashboardComponent, TasksComponent, GraphComponent, AllUsersComponentComponent,
    CreateTaskComponent],
  templateUrl: './admin-layout-component.component.html',
  styleUrl: './admin-layout-component.component.scss'
})

export class AdminLayoutComponentComponent {

  constructor(private router : Router){}

}
