import { Component, Inject, PLATFORM_ID } from '@angular/core';
import { SimpleNotification } from '../../dashboardmodel/simple-notification';
import { NotificationServiceService } from '../notification-service.service';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { getUserIdFromToken } from '../../shared/auth-utils.ts';

@Component({
  selector: 'app-notification-preview',
  standalone:true,
  imports: [CommonModule, RouterLink, RouterOutlet],
  templateUrl: './notification-preview.component.html',
  styleUrl: './notification-preview.component.scss'
})
export class NotificationPreviewComponent {

  
  notification: SimpleNotification [] = [];
  errorMsg: any;

  constructor(private service: NotificationServiceService, @Inject(PLATFORM_ID) private platformId: Object,
      private router: Router){}

  ngOnInit(): void {
   if (isPlatformBrowser(this.platformId)) {
    const token = localStorage.getItem('token');
        if (!token) {
          this.router.navigate(['/login']);
          return;
        }
        const userId = getUserIdFromToken();
          console.log('User ID from token:', userId);
  if (userId !== null && !isNaN(userId)) {
  this.service.getUserNotifications(userId).subscribe({
    next: (response) => {
      this.notification = response.slice(0,3);
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
