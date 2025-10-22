import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { ProfileComponent } from "../profile/profile.component";
import { NotificationComponent } from "../notification/notification.component";
import { NotificationPreviewComponent } from "../notification-preview/notification-preview.component";
import { AllTasksPreviewComponent } from "../all-tasks-preview/all-tasks-preview.component";
import { UserTasksPreviewComponent } from "../user-tasks-preview/user-tasks-preview.component";
import { ActivityComponent } from '../activity/activity.component';

@Component({
  selector: 'app-user-dashboard-content',
  standalone:true,
  imports: [CommonModule, RouterOutlet, ProfileComponent, NotificationComponent, NotificationPreviewComponent, 
    UserTasksPreviewComponent, ActivityComponent, RouterLink],
  templateUrl: './user-dashboard-content.component.html',
  styleUrl: './user-dashboard-content.component.scss'
})
export class UserDashboardContentComponent {

}
