import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LoginResponse } from './shared/login-response';

@Injectable({
  providedIn: 'root'
})
export class AuthServiceService {
  private baseUrl: string = 'http://localhost:8082/api/auth/';

  constructor(private http: HttpClient) {}

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  registerUser(userData: any): Observable<any> {
    const url = `${this.baseUrl}register`;
    return this.http.post(url, userData, {
      responseType: 'text'
    });
  }

  verifyOtp(email: string, otp: string): Observable<string> {
    const payload = { email, otp };
    return this.http.post(`${this.baseUrl}verify-otp`, payload, {
      responseType: 'text'
    });
  }

  userLogin(userName: string, password: string): Observable<LoginResponse> {
    const payload = { userName, password };
    return this.http.post<LoginResponse>(`${this.baseUrl}user/login`, payload);
  }

  adminLogin(userName: string, password: string): Observable<LoginResponse> {
    const payload = { userName, password };
    const url = `${this.baseUrl}admin/login`;
    return this.http.post<LoginResponse>(url, payload);
  }

  // 🔐 Example of a protected endpoint (if needed later)
  getAuthenticatedUserProfile(): Observable<any> {
    const url = `${this.baseUrl}profile`;
    return this.http.get(url, { headers: this.getAuthHeaders() });
  }

  sendOtp(email:string): Observable<any>{
    const url = `${this.baseUrl}send-otp?email=${encodeURIComponent(email)}`;
      return this.http.get(url, { responseType: 'text' });
  }

  updatePassword(form: any): Observable<any>{
    console.log("form from service: ", form);
    const url = `${this.baseUrl}update-password`;
    return this.http.put(url, form,{
      responseType: 'text'
    });
  }
}