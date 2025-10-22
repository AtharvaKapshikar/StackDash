import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AdminDashboardServiceService } from '../admin-dashboard/admin-dashboard-service.service';
import { RoleType } from '../../dashboardmodel/enum/roletype';
import { RoleDto } from '../../dashboardmodel/roledto';

@Component({
  selector: 'app-setting',
  standalone:true,
  imports: [CommonModule, RouterLink],
  templateUrl: './setting.component.html',
  styleUrl: './setting.component.scss'
})
export class SettingComponent {

}
