import { CommonModule, isPlatformBrowser } from '@angular/common';
import { Component, Inject, OnInit, PLATFORM_ID } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { NotificationServiceService } from '../notification-service.service';
import { SimpleNotification } from '../../dashboardmodel/simple-notification';
import { error } from 'console';
import { setMaxListeners } from 'stream';
import { getUserIdFromToken } from '../../shared/auth-utils.ts';

@Component({
  selector: 'app-notification',
  standalone:true,
  imports: [CommonModule, RouterLink, RouterOutlet],
  templateUrl: './notification.component.html',
  styleUrl: './notification.component.scss'
})
export class NotificationComponent implements OnInit {


  notification: SimpleNotification [] = [];
  errorMsg: any;

  constructor(private service: NotificationServiceService, @Inject(PLATFORM_ID) private platformId: Object,
  private router:Router){}

  ngOnInit(): void {
   if (isPlatformBrowser(this.platformId)) {
    const token = localStorage.getItem('token');
    if (!token) {
      this.router.navigate(['/login']);
      return;
    }
    const userId = getUserIdFromToken();
      console.log('User ID from token:', userId);

console.log("user id ng:", userId);
  if (userId !== null && !isNaN(userId)) {
  this.service.getUserNotifications(userId).subscribe({
    next: (response) => {
      this.notification = response;
    },
    error: (er) => {
      this.errorMsg = er;
      console.log("error:", er);
    }
  });
}

   }
  }

}
