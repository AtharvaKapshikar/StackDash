import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterOutlet, RouterLink, Route, Router } from '@angular/router';
import { AuthServiceService } from '../../auth-service.service';
import { response } from 'express';
import { TimeScale } from 'chart.js';

@Component({
  selector: 'app-forgot-password',
  standalone:true,
  imports: [CommonModule, FormsModule, RouterOutlet, ReactiveFormsModule, RouterLink],
  templateUrl: './forgot-password.component.html',
  styleUrl: './forgot-password.component.scss'
})
export class ForgotPasswordComponent {
[x: string]: any;
submitted=false;
emailSubmitted=false;
errorMessage = '';
successMessage= '';
  updatePassForm: FormGroup;


constructor(private fb: FormBuilder, private service: AuthServiceService, private router:Router) {
  this.updatePassForm = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    otp: ['', Validators.required],
    password: ['', [Validators.required, Validators.minLength(6), 
        Validators.pattern(/^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{6,}$/)]],
      confirmPassword: ['', Validators.required],
  });
}

sendVerification() {
  this.emailSubmitted = true;
  const emailControl = this.updatePassForm.get('email');

  if (emailControl?.valid) {
    const email = emailControl.value;

    this.service.sendOtp(email).subscribe({
      next: (response) => {
        console.log("OTP sent");
        this.successMessage = response;
        setTimeout(() => {
          this.successMessage = '';
        }, 5000);
        this.emailSubmitted = false;
      },
      error: (err) => {
        console.log("Error occurred while sending OTP");
        this.errorMessage = err?.error?.message || 'Failed to send OTP or Email is invalid';
        setTimeout(() => {
          this.errorMessage = '';
        }, 5000);
        this.emailSubmitted = false;
      }
    });
  } else {
    this.errorMessage = 'Please enter a valid email';
    setTimeout(() => {
      this.errorMessage = '';
    }, 5000);
    this.emailSubmitted = false;
  }
}

onSubmit() {
  this.submitted=true;

  if(this.updatePassForm.valid){

  const form = this.updatePassForm.value;
  console.log("form : ", form);
  
  if (this.updatePassForm.valid && form.password === form.confirmPassword) {
    this.service.updatePassword(form).subscribe({
      next: (response) => {
        console.log("Password updated");
        this.successMessage = response;

        setTimeout(() => {
          this.successMessage = '';
        }, 5000);

        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error("Error occurred while updating password:", err);
        this.errorMessage = err.error || "Something went wrong";

        setTimeout(() => {
          this.errorMessage = '';
        }, 5000);
      }
    });
  } else {
    this.errorMessage = "Passwords don't match";

    setTimeout(() => {
      this.errorMessage = '';
    }, 5000);
  }
}
}

}
