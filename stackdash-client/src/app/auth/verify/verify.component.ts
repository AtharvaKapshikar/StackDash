import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthServiceService } from '../../auth-service.service';
import { Router } from '@angular/router';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-verify',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule, CommonModule],
  providers:[AuthServiceService],
  templateUrl: './verify.component.html',
  styleUrl: './verify.component.scss'
})
export class VerifyComponent {

  otpForm: FormGroup;
  submitted = false;
  errorMessage = "";
  successMessage="";
  email: string ;

  constructor(private fb : FormBuilder, private service: AuthServiceService, private router: Router){

    const nav = this.router.getCurrentNavigation();
    this.email = nav?.extras?.state?.['email'] || '';
      this.otpForm = this.fb.group(
        {
          
        otp:['', [Validators.required,Validators.pattern(/^\d{6}$/)]]
      });
  }


  onVerify(): void {
  this.submitted = true;

  if (this.otpForm.valid) {
    const otp = this.otpForm.value.otp;
    console.log('Entered OTP:', otp);
    console.log('Email for verification:', this.email);

    this.service.verifyOtp(this.email, otp).subscribe({
      next: (response: string) => {
        console.log('OTP verification success:', response);

        this.errorMessage = '';
        this.successMessage = '✅ OTP verified successfully. Redirecting...';

        setTimeout(() => {
          this.successMessage='';
          this.router.navigate(['/user/dashboard']);
        }, 2000);
      },
      error: (error: { status: number; error: string; }) => {
        console.error('OTP verification failed:', error);

        if (error.status === 401) {
          this.errorMessage = error.error || 'Invalid OTP. Please try again.';
        } else if (error.status === 0) {
          this.errorMessage = 'Server unreachable. Check your connection.';
        } else {
          this.errorMessage = 'Something went wrong. Please try again.';
        }

        this.successMessage = '';
      }
    });
  } else {
    this.errorMessage = 'Please enter the OTP and accept the terms.';
    setTimeout(() => {
          this.errorMessage='';
        }, 2000);
    this.successMessage = '';
  }
}

  resendOtp() {
    // Trigger resend logic
    //formgr
     this.service.sendOtp(this.email).subscribe({
    next: (response) => {
      console.log("otp sent");
      this.successMessage = response;
      setTimeout(() => {
        this.successMessage = '';
      },5000);
    },
    error: (err) => {
      console.log("error occured while send otp");
      this.errorMessage = err;
      setTimeout(() => {
        this.errorMessage = '';
      },5000);
    }
  })
}
}
