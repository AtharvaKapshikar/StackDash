import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SessionLog } from './session-log';

@Injectable({
  providedIn: 'root'
})
export class SessionLogServiceService {

  constructor(private http: HttpClient) {}

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    console.log("Token for session service: ", token)
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  getUserSessionLog(userId: number): Observable<any> {
    console.log("calling user session log api:", userId);
    const url = `http://localhost:8082/api/session-history/${userId}`;
    return this.http.get<SessionLog>(url, { headers: this.getAuthHeaders() });
  }
}