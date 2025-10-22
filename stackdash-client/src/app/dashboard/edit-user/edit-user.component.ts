import { CommonModule, isPlatformBrowser } from '@angular/common';
import { Component, Inject, OnInit, PLATFORM_ID } from '@angular/core';
import { AdminDashboardServiceService } from '../admin-dashboard/admin-dashboard-service.service';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { RoleDto } from '../../dashboardmodel/roledto';
import { RoleType } from '../../dashboardmodel/enum/roletype';
import { User } from '../../dashboardmodel/user';

@Component({
  selector: 'app-edit-user',
  standalone:true,
  imports: [CommonModule, FormsModule,RouterLink],
  templateUrl: './edit-user.component.html',
  styleUrl: './edit-user.component.scss'
})
export class EditUserComponent{

  message: string | null = null;
messageType: 'success' | 'error' = 'success';
availableRoles: RoleDto[] = [];

user:User = {} as User;
  userId: number | null =null;

  constructor(private service: AdminDashboardServiceService, private route: ActivatedRoute, 
    private router: Router,@Inject(PLATFORM_ID) private platformId: Object){}

  ngOnInit() {
  if (isPlatformBrowser(this.platformId)) {
    const token = localStorage.getItem('token');
    if (!token) {
      this.router.navigate(['/login']);
      return;
    }

    const paramId = this.route.snapshot.paramMap.get('userId');
this.userId = paramId ? Number(paramId) : null;
console.log('Editing user ID from route:', this.userId);

    if (this.userId !== null) {
      // First get roles, then get user
      this.service.getAllRoles().subscribe((roles) => {
        this.availableRoles = roles;
        console.log("Roles:", roles);

        this.service.getUser(this.userId!).subscribe({
          next: (response) => {
            this.user = response;
            console.log('Raw user data:', this.user);

            // Map user.roles (strings) to RoleDto objects
            this.user.roles = this.availableRoles.filter(role =>
              response.roles.includes(role.name)
            );

            console.log("Mapped user:", this.user);
          },
          error: (err) => {
            console.error('Error fetching user:', err);
            this.message = 'Failed to load user: ' + err.error;
            this.messageType = 'error';
          }
        });
      });
    }
  }
}

  editUser() {

  if (!this.user) {
    console.error('User is undefined. Cannot update.');
    return;
  }

  this.service.updateUser(this.user).subscribe({
    next: (response) => {
      console.log("User from ts:",this.user)
      this.message = '✅ User Details Updated successfully!';
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

onRoleChange(event: Event, role: RoleDto) {
  const checked = (event.target as HTMLInputElement).checked;
  if (checked) {
    this.user.roles.push(role);
    console.log("Slected :", role)
  } else {
    this.user.roles = this.user.roles.filter(r => r.id !== role.id);
  }
}

isRoleSelected(role: RoleDto): boolean {
  return this.user.roles.some(r => r.name === role.name);
}
}
