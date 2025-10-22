
import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import {  Router, RouterLink } from '@angular/router';
import { AuthServiceService } from '../../auth-service.service';
import { HttpClientModule } from '@angular/common/http';


@Component({
  selector: 'app-user-registration',
  standalone:true,
  imports: [FormsModule, CommonModule, ReactiveFormsModule, HttpClientModule, RouterLink],
   providers: [AuthServiceService],
  templateUrl: './user-registration.component.html',
  styleUrl: './user-registration.component.scss'
})


export class UserRegistrationComponent {

  step = 1;
  submitted = false;
  errorMessage: string = ''; // Error messages for user feedback
  details="hidden";
  userData : any;
  selectedFile: File | null = null;
  successMessage:string='';
  email="";
  isLoading = false;


  registerForm : FormGroup;

  constructor(private fb: FormBuilder, private router: Router, private service:AuthServiceService){
    this.registerForm =  this.fb.group({
      firstName:['', Validators.required],
      lastName:['',Validators.required],
      age : ['', [Validators.required, Validators.min(18)]],
      city : ['',Validators.required],
      mobileNumber:['', [Validators.required, Validators.pattern(/^\d{10}$/)]],
      email:['',[Validators.required, Validators.email]],
      userName: this.usernameControl,
      password: ['', [Validators.required, Validators.minLength(6), 
        Validators.pattern(/^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{6,}$/)]],
      confirmPassword: ['', Validators.required],
      designation: [''],
      profilePicture: [null],
      enabled: ['', Validators.requiredTrue],
      roles: this.fb.array([{ name: 'USER' }])
    }, { validators: (group: AbstractControl): ValidationErrors | null => {
    const password = group.get('password')?.value;
    const confirmPassword = group.get('confirmPassword')?.value;
    return password === confirmPassword ? null : { passwordMismatch: true };
  }});
  }

  nextStep() {
  this.submitted = true;

  // Validate current step fields before moving forward
  if (this.step === 1) {
    if (
      this.registerForm.get('firstName')?.invalid ||
      this.registerForm.get('lastName')?.invalid ||
      this.registerForm.get('age')?.invalid ||
      this.registerForm.get('city')?.invalid ||
      this.registerForm.get('mobileNumber')?.invalid
    ) {
      return; 
    }
  }

  if (this.step === 2) {
    if (
      this.registerForm.get('email')?.invalid ||
      this.registerForm.get('userName')?.invalid ||
      this.registerForm.get('password')?.invalid ||
      this.registerForm.get('confirmPassword')?.invalid
    ) {
      return; // Don't advance if step 2 is invalid
    }
  }

  this.step++;
  this.submitted = false; // Reset for next step

  if (this.step === 3) {
  const designationCtrl = this.registerForm.get('designation');
  const profileCtrl = this.registerForm.get('profilePicture');

  if (
    designationCtrl?.invalid ||
    !profileCtrl?.value // optional: enforce file upload
  ) {
    this.submitted = true;
    return; // prevent submission if invalid
  }

  // Optionally: validate file type or size
  const file = profileCtrl.value;
  if (file && file.type !== 'image/png' && file.type !== 'image/jpeg') {
    alert('Only PNG or JPEG files are allowed');
    return;
  }

  this.onSubmit(); // final submission
}

}


  prevStep(){
    if(this.step > 1) {
      this.step--;
    }
  }

  onSubmit() {
  if (this.registerForm.valid && this.selectedFile) {
    this.isLoading = true;
    const formData = new FormData();


    // Append the profile picture file
    formData.append('profilePicture', this.selectedFile);

    // Append the user object as JSON
    formData.append('user', new Blob([JSON.stringify(this.registerForm.value)], {
      type: 'application/json'
    }));

    // Call the backend service
    this.service.registerUser(formData).subscribe(
      response => {
        console.log('Registration successful:', response);
        this.router.navigate(['/verify'], { state: { email: this.registerForm.get('email')?.value } });
         //this.successMessage = 'Registration successful!';
        this.errorMessage = '';
        
      },
      error => {
        this.isLoading=false;
        if (error.status === 409) {
          this.errorMessage = error.error ; // "Email already registered" or "Username already taken"
        } else {
        this.errorMessage = 'Something went wrong. Please try again.';
        }
        console.error('Registration failed:', error);
      }
    );
  } else {
    this.errorMessage = 'Please fill all required fields and accept the term and policy.';
    this.isLoading=false;
  }
}


  usernameControl = new FormControl('');
  isUsernameAvailable: boolean | null = null;

  onFileSelected(event: any): void {
  const file = event.target.files[0];
  if (file) {
    this.selectedFile = file;
    console.log('Selected file:', file);
  }
}


}
