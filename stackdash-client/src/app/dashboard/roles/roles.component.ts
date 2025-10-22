import { Component } from '@angular/core';
import { AdminDashboardServiceService } from '../admin-dashboard/admin-dashboard-service.service';
import { RoleDto } from '../../dashboardmodel/roledto';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-roles',
  standalone:true,
  imports: [CommonModule, RouterLink, FormsModule],
  templateUrl: './roles.component.html',
  styleUrl: './roles.component.scss'
})
export class RolesComponent {
  
    role?:RoleDto[];
    errorMsg="";
  
    constructor(private service:AdminDashboardServiceService){}

  ngOnInit(){
    this.service.getAllRoles().subscribe(roles => {
      this.role = roles;// Show only top 5
    },
    error => {
      this.errorMsg = error.error;
    }
    );
  }
}
