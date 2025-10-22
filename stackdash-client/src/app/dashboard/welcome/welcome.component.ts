import { Component, Input } from '@angular/core';
import { DashboardDto } from '../dashboard-dto';
import { CommonModule } from '@angular/common';
import { Dashboarduserdto } from '../dashboarduserdto';
import { Router, RouterLink, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-welcome',
  standalone:true,
  imports: [CommonModule,RouterLink,RouterOutlet],
  templateUrl: './welcome.component.html',
  styleUrl: './welcome.component.scss'
})
export class WelcomeComponent {

  constructor(private router:Router){}

  @Input() user?: Dashboarduserdto;
  ngOnChanges() {
  console.log('Received user in WelcomeComponent:', this.user);
}

}
