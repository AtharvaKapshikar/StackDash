import { CommonModule, isPlatformBrowser } from '@angular/common';
import { Component, Inject, PLATFORM_ID } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { Router, RouterModule } from '@angular/router'; 
import { AuthServiceService } from '../../auth-service.service';
import { LoginResponse } from '../../shared/login-response';
import { jwtDecode } from 'jwt-decode';
import { CustomJwtPayload } from '../../dashboardmodel/custom-jwt-payload';

@Component({
  selector: 'app-user-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    RouterModule 
  ],
  templateUrl: './user-login.component.html',
  styleUrls: ['./user-login.component.scss']
})
export class UserLoginComponent {
  loginForm: FormGroup;
  userName = '';
  password = '';
  errorMessage = '';
  successMessage = '';
  submitted = false;
  userId?: number;

  constructor(
    private titleService: Title,
    private router: Router,
    private fb: FormBuilder,
    private service: AuthServiceService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    this.titleService.setTitle('StackDash Login');
    this.loginForm = this.fb.group({
      userName: ['', Validators.required],
      password: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      const token = localStorage.getItem('token');
      console.log('Token:', token);
    }
  }

  onSubmit() {
    this.submitted = true;

    if (this.loginForm.valid) {
      this.userName = this.loginForm.get('userName')?.value;
      this.password = this.loginForm.get('password')?.value;

      this.service.userLogin(this.userName, this.password).subscribe({
        next: (response: LoginResponse) => {
          this.errorMessage = '';
          this.successMessage = 'Logged in successfully...';

          if (isPlatformBrowser(this.platformId)) {
            localStorage.setItem('token', response.token);
            const decoded = jwtDecode<CustomJwtPayload>(response.token);
            this.userId = decoded.userId; // Set userId
            const roles = decoded.roles || [];

console.log('User ID from token:', this.userId);
console.log('Roles from token:', roles);        

            console.log('Roles:', roles);

            // Explicitly trigger navigation inside Angular zone
            setTimeout(() => {
              if (roles.includes('ADMIN')) {
                this.router.navigate(['/admin/dashboard']);
              } else {
                this.router.navigate(['/user/dashboard']);
              }
            });
          }
        },
        error: (error) => {
          this.errorMessage = 'Login failed. Please check your credentials.';
          console.error('Login error:', error);
        },
      });
    }
  }

  registerUser() {
    this.router.navigate(['/register']);
  }
}
