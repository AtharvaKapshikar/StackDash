import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { response, Router } from 'express';
import { AdminDashboardServiceService } from '../admin-dashboard/admin-dashboard-service.service';
import { error } from 'console';
import { User } from '../../dashboardmodel/user';

@Component({
  selector: 'app-all-users-preview',
  standalone:true,
  imports: [CommonModule,RouterLink],
  templateUrl: './all-users-preview.component.html',
  styleUrl: './all-users-preview.component.scss'
})
export class AllUsersPreviewComponent implements OnInit {

recentUsers: User[] = [];
errorMsg ="";

  constructor( private service: AdminDashboardServiceService){}

  ngOnInit(){
     this.service.getLatestUserList().subscribe(users => {
      console.log("User list : ", users);
      this.recentUsers = users;
      this.recentUsers = users.slice(0, 5); // Show only top 5
},
error => {
  this.errorMsg = error.error;
}
);

}

 editUser(user:User){

  }

  deleteUser(userId:number){

  }

  getRoleClass(role: string): string {
  switch (role) {
    case 'ADMIN': return 'role-admin';
    case 'USER': return 'role-user';
    case 'MODERATOR': return 'role-moderator';
    default: return 'role-default';
  }
}

}
