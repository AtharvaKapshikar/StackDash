import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { UserLoginComponent } from "../user-login/user-login.component";

@Component({
  selector: 'app-logout',
  standalone:true,
  imports: [CommonModule, RouterOutlet, RouterLink, UserLoginComponent],
  templateUrl: './logout.component.html',
  styleUrl: './logout.component.scss'
})
export class LogoutComponent implements OnInit {

  constructor(private router: Router,){}

  ngOnInit(){
    
     localStorage.removeItem('token'); // or sessionStorage
     this.router.navigate(['/login']); // redirect to login page
      alert('You have been logged out.');
  }
}
