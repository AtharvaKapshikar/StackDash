import { Component, Inject, Input, OnInit, PLATFORM_ID } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, Validators } from '@angular/forms';
import { Userprofile } from '../../dashboardmodel/userprofile';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { getUserIdFromToken } from '../../shared/auth-utils.ts';
import { User } from '../../dashboardmodel/user';
import { DashboardServiceService } from '../dashboard-service.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss',
  imports: [FormsModule, CommonModule, RouterOutlet, RouterLink]
})
export class ProfileComponent implements OnInit {
  
  profileImageUrl: any;
  user: Userprofile | null = null;
  userId?: number | null = null;
  message: string | null = null;
  messageType: 'success' | 'error' = 'success';
  profileForm: FormGroup;
  rolesList: any;
  isAdmin = false;

  constructor(
    private service: DashboardServiceService,
    @Inject(PLATFORM_ID) private platformId: Object,
    private fb: FormBuilder,
    private router: Router
  ) {
    this.profileForm = this.fb.group({
      userId: [null],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      age: [null, [Validators.required, Validators.min(1)]],
      city: [''],
      mobileNumber: ['', Validators.pattern(/^[0-9]{10}$/)],
      email: ['', [Validators.required, Validators.email]],
      designation: [''],
      active: [false],
      verified: [false],
      roles: [[]],
      profilePicture: [null]
    });
  }

  ngOnInit(): void {
  if (isPlatformBrowser(this.platformId)) {
    const token = localStorage.getItem('token');
    if (!token) {
      this.router.navigate(['/login']);
      return;
    }

    this.userId = getUserIdFromToken();
    console.log('User ID from token:', this.userId);

    if (this.userId !== null) {
      this.service.getUser(this.userId).subscribe({
        next: (response) => {
          this.user = response;
          console.log('Profile data:', this.user);
          // Patch form here...
        },
        error: (err) => {
          this.message = 'Failed to load profile: ' + err;
          this.messageType = 'error';
        }
      });
    } else {
      this.message = 'Invalid token: userId not found';
      this.messageType = 'error';
    }
  }
}

  submitForm() {
    if (this.profileForm.valid) {
      const updatedUser = this.profileForm.value;
      this.service.updateUser(updatedUser).subscribe({
        next: () => {
          this.message = 'Profile updated successfully';
          this.messageType = 'success';
        },
        error: (err) => {
          this.message = 'Update failed: ' + err;
          this.messageType = 'error';
        }
      });
    } else {
      this.message = 'Please correct the errors in the form';
      this.messageType = 'error';
    }
  }

  onFileChange(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.profileForm.patchValue({ profilePicture: file });
    }
  }

  onRoleChange(event: any) {
    const roles = this.profileForm.value.roles;
    if (event.target.checked) {
      roles.push(event.target.value);
    } else {
      const index = roles.indexOf(event.target.value);
      if (index >= 0) roles.splice(index, 1);
    }
    this.profileForm.patchValue({ roles });
  }

  editUser(user: User) {
    console.log('Editing user:', user.userId);
    this.router.navigate([`user/update/${user.userId}`]);
  }
}