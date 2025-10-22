
import { Routes } from '@angular/router';
import { AdminLayoutComponentComponent } from './shared/admin-layout-component/admin-layout-component.component';
import { AllUsersComponentComponent } from './dashboard/all-users-component/all-users-component.component';
import { TasksComponent } from './dashboard/tasks/tasks.component';
import { AdminDashboardComponent } from './dashboard/admin-dashboard/admin-dashboard.component';
import { CreateTaskComponent } from './dashboard/create-task/create-task.component';
import { EditTaskComponent } from './dashboard/edit-task/edit-task.component';
import { SettingComponent } from './dashboard/setting/setting.component';
import { EditUserComponent } from './dashboard/edit-user/edit-user.component';
import { WelcomeComponent } from './dashboard/welcome/welcome.component';
import { NotificationComponent } from './dashboard/notification/notification.component';
import { ReportComponent } from './dashboard/report/report.component';
import { ProfileComponent } from './dashboard/profile/profile.component';
import { AdminProfileComponent } from './dashboard/admin-profile/admin-profile.component';
import { UserDashboardComponent } from './dashboard/user-dashboard.component';
import { UserDashboardContentComponent } from './dashboard/user-dashboard-content/user-dashboard-content.component';
import { UserAllTaskComponent } from './dashboard/user-all-task/user-all-task.component';
import { UpdateUserDataComponent } from './dashboard/update-user-data/update-user-data.component';
import { UpdateUserTaskComponent } from './dashboard/update-user-task/update-user-task.component';
import { UserSettingComponent } from './dashboard/user-setting/user-setting.component';
import { RolesComponent } from './dashboard/roles/roles.component';

export const routes: Routes = [
   {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  },
  {
    path: 'register',
    loadComponent: () =>
      import('./auth/user-registration/user-registration.component').then(m => m.UserRegistrationComponent)
  },
  {
    path: 'verify',
    loadComponent: () =>
      import('./auth/verify/verify.component').then(m => m.VerifyComponent)
  },
  {
    path: 'login',
    loadComponent: () =>
      import('./auth/user-login/user-login.component').then(m => m.UserLoginComponent)
  },
  {
    path: 'admin/login',
    loadComponent: () =>
      import('./auth/admin-login/admin-login.component').then(m => m.AdminLoginComponent)
  },
  // {
  //   path: 'user/dashboard',
  //   loadComponent: () =>
  //     import('./dashboard/user-dashboard.component').then(m => m.UserDashboardComponent)
  // },
  // {
  //  path: 'profile',
  //   loadComponent: () =>
  //     import('./dashboard/profile/profile.component').then(m => m.ProfileComponent)
  // },
  {
   path: 'forgot-password',
    loadComponent: () =>
      import('./auth/forgot-password/forgot-password.component').then(m => m.ForgotPasswordComponent)
  },
  {
  path: 'logout',
    loadComponent: () =>
      import('./auth/logout/logout.component').then(m => m.LogoutComponent)
  },
  {
    path: 'user',
    component: UserDashboardComponent,
    children: [
      { path: 'dashboard', component: UserDashboardContentComponent },
      // { path: 'users', component: AllUsersComponentComponent },
       {path:'update/:id', component: UpdateUserDataComponent},
       { path: 'tasks', component: UserAllTaskComponent },
      // {path: 'create/task', component: CreateTaskComponent},
       {path:'tasks/edit/:id', component: UpdateUserTaskComponent},
       {path:'setting', component: UserSettingComponent},
       {path:'notification', component:NotificationComponent},
       {path:'report', component: ReportComponent},
       {path:'profile', component: ProfileComponent}

    ]
},
   {
    path: 'admin',
    component: AdminLayoutComponentComponent,
    children: [
      { path: 'dashboard', component: AdminDashboardComponent },
      { path: 'users', component: AllUsersComponentComponent },
      {path:'edit/user/:userId', component: EditUserComponent},
      { path: 'tasks', component: TasksComponent },
      {path: 'create/task', component: CreateTaskComponent},
      {path:'tasks/edit/:id', component: EditTaskComponent},
      {path:'setting', component: SettingComponent},
      {path:'notify', component:NotificationComponent},
      {path:'report', component: ReportComponent},
      {path:'profile', component: AdminProfileComponent},
      {path:'roles', component:RolesComponent}

    ]
}

];
