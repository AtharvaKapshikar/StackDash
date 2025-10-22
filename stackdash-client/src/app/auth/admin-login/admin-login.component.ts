import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { AuthServiceService } from '../../auth-service.service';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Title } from '@angular/platform-browser';
import { LoginResponse } from '../../shared/login-response';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-admin-login',
  standalone:true,
  providers:[AuthServiceService],
  imports: [CommonModule,ReactiveFormsModule,FormsModule,RouterModule],
  templateUrl: './admin-login.component.html',
  styleUrl: './admin-login.component.scss'
})
export class AdminLoginComponent {

  loginForm : FormGroup;
  userName:string = " ";
  password:string= "";
  errorMessage="";
  successMessage="";

  submitted= false;

  constructor(private titleService : Title,private router:Router, private fb: FormBuilder, private service: AuthServiceService ){
      this.titleService.setTitle("StackDash Admin Login")
      this.loginForm = this.fb.group({
        userName:['',Validators.required],
        password:['',Validators.required]
      })
  }



  onSubmit(){
    this.submitted=true;

    if(this.loginForm.valid){
    this.userName = this.loginForm.get('userName')?.value;
    this.password = this.loginForm.get('password')?.value;
    console.log("Admin username >> into the onSubmit method", this.userName);
    this.service.adminLogin(this.userName,this.password).subscribe({
      next: (response: LoginResponse) => {
        console.log("Admin logged in successfully");
        this.errorMessage='';
        this.successMessage="Logged in successfully..."
        localStorage.setItem('userId', response.userId.toString());
        this.router.navigate(['/admin/dashboard']);
        //this.router.navigate(['dashboard', this.loginForm.get('userName')?.value])
      },
      error: (error: { status: number, error: string, }) => {
        console.log('Failed to login')
        this.successMessage = '';
        if(error.status === 404){
          this.errorMessage= error.error || 'Admin username not found. Please register!';
        }else if(error.status === 401){
          this.errorMessage = error.error || 'Invalid credentials!';
        }else{
          this.errorMessage = 'Please enter valid username and password'
        }
      }
    })
  }else{
    this.errorMessage = 'Please enter valid username and password';
    this.successMessage='';
  }
    //this.router.navigate(['/'])
  }



}
