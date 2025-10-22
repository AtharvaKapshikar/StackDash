import { Component } from '@angular/core';
import {
  NavigationCancel,
  NavigationEnd,
  NavigationError,
  NavigationStart,
  Router,
  RouterOutlet
} from '@angular/router';
import {
  UserRegistrationComponent
} from './auth/user-registration/user-registration.component';
import {
  UserLoginComponent
} from './auth/user-login/user-login.component';
import {
  FormsModule,
  ReactiveFormsModule
} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { VerifyComponent } from './auth/verify/verify.component';
import { HttpClient } from '@angular/common/http';
import { AuthServiceService } from './auth-service.service';
import { UserDashboardComponent } from './dashboard/user-dashboard.component';
import { ProfileComponent } from './dashboard/profile/profile.component';

@Component({
  selector: 'app-root',
  imports: [
    RouterOutlet,
    UserRegistrationComponent,
    UserLoginComponent,
    FormsModule,
    ReactiveFormsModule,
    CommonModule,
    VerifyComponent,
    UserDashboardComponent,
    ProfileComponent
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'stackdash-client';
  loading = false;

  constructor(private router: Router) {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationStart) {
        setTimeout(() => this.loading = true);
      }
      if (
        event instanceof NavigationEnd ||
        event instanceof NavigationCancel ||
        event instanceof NavigationError
      ) {
        setTimeout(() => this.loading = false);
      }
    });
  }
}