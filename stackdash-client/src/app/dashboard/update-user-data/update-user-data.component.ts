import { Component } from '@angular/core';
import { RoleDto } from '../../dashboardmodel/roledto';
import { User } from '../../dashboardmodel/user';
import { AdminDashboardServiceService } from '../admin-dashboard/admin-dashboard-service.service';
import { ActivatedRoute, Router, RouterLink, RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DashboardServiceService } from '../dashboard-service.service';

@Component({
  selector: 'app-update-user-data',
  imports: [CommonModule, FormsModule, RouterOutlet, RouterLink],
  templateUrl: './update-user-data.component.html',
  styleUrl: './update-user-data.component.scss'
})
export class UpdateUserDataComponent {

  message: string | null = null;
messageType: 'success' | 'error' = 'success';
availableRoles: RoleDto[] = [];

user:User = {} as User;

  constructor(private service: DashboardServiceService, private route: ActivatedRoute, private router: Router){}

  ngOnInit(){
    const id = this.route.snapshot.paramMap.get('id')
    console.log("user id ng: ", id)
    const userid = id;
     if (id) {

      this.service.getAllRoles().subscribe((roles) => {
      this.availableRoles = roles;
      });

      this.service.getUser(+id).subscribe(user => {
      console.log("User from service call :", user);
      this.user = user
      this.user.roles = this.availableRoles.filter(role =>
          user.roles.includes(role.name)
        );
       console.log("user : ", user)
      });
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
