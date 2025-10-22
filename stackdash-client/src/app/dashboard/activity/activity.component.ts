import { Component, Input, OnInit } from '@angular/core';
import { DashboardDto } from '../dashboard-dto';
import { Dashboarduserdto } from '../dashboarduserdto';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpRequest } from '@angular/common/http';
import { SessionLog } from '../session-log';
import { SimpleNotification } from '../../dashboardmodel/simple-notification';
import { error } from 'console';
import { NotificationServiceService } from '../notification-service.service';
import { SessionLogServiceService } from '../session-log-service.service';
import { isPlatformBrowser } from '@angular/common';
import { Inject, PLATFORM_ID } from '@angular/core';
import { getUserIdFromToken } from '../../shared/auth-utils.ts';

@Component({
  selector: 'app-activity',
  standalone:true,
  imports: [CommonModule],
  templateUrl: './activity.component.html',
  styleUrl: './activity.component.scss'
})
export class ActivityComponent implements OnInit {

 userId: number | null = null;
  sessionLogs: SessionLog[] = [];
  simpleNotifications: SimpleNotification[] = [];
  errorMessage:any;

  constructor(private http:HttpClient, private notifyService: NotificationServiceService, 
    private sessionService: SessionLogServiceService, @Inject(PLATFORM_ID) private platformId: Object){}

  @Input() user?: Dashboarduserdto;
    ngOnChanges() {
    console.log('Received user in activity component:', this.user);
  }


  ngOnInit(){

      if (isPlatformBrowser(this.platformId)) {
        this.userId = getUserIdFromToken();
        console.log('User ID from token: activity', this.userId);

      if (!this.userId || isNaN(this.userId)) {
        console.warn('Invalid or missing userId in token');
        return;
      }

      this.sessionService.getUserSessionLog(this.userId).subscribe(data => {
      console.log("from get session api called ", this.userId, data);
    this.sessionLogs = data;
    },
    error => {
    console.log("error while calling session history api>>");
    this.errorMessage = error.error;
  });

  this.notifyService.getUserNotifications(this.userId)
    .subscribe(data => {
      console.log("Fetched notifications:", data);
      this.simpleNotifications = data;
    },
    error => {
      console.log("error occured while fetching notifications:");
      this.errorMessage = error.error;
    });
  // use token safely
    } else {
    console.log('SSR: skipping localStorage access');
  }

    
}
}
