import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../dashboardmodel/user';
import { Task } from '../dashboardmodel/task';
import { RoleDto } from '../dashboardmodel/roledto';

@Injectable({ providedIn: 'root' })
export class DashboardServiceService {
  private baseUrl: string = 'http://localhost:8082/api/user/';

  constructor(private http: HttpClient) {}

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  getDashboardData(id: any): Observable<any> {
    console.log('Token before dashboard request:', localStorage.getItem('token'));
    const url = `${this.baseUrl}getUser/${id}`;
    return this.http.get(url, { headers: this.getAuthHeaders() });
  }

  updateUser(user: User): Observable<any> {
    const url = `${this.baseUrl}update`;
    return this.http.put(url, user, {
      headers: this.getAuthHeaders(),
      responseType: 'text'
    });
  }

  getTask(taskId: number): Observable<any> {
    const url = `${this.baseUrl}task/${taskId}`;
    return this.http.get(url, { headers: this.getAuthHeaders() });
  }

  editTask(task: Task): Observable<any> {
    const url = `${this.baseUrl}update-task`;
    return this.http.put(url, task, {
      headers: this.getAuthHeaders(),
      responseType: 'text'
    });
  }

   getUserTasks(userId: Number): Observable<any> {
    return this.http.get(`${this.baseUrl}tasks/${userId}`, { headers: this.getAuthHeaders() });
  }

   getUser(userId: number): Observable<any> {
    return this.http.get(`${this.baseUrl}getUser/${userId}`, { headers: this.getAuthHeaders() });
  }

  getAllRoles(): Observable<RoleDto[]> {
      return this.http.get<RoleDto[]>(`${this.baseUrl}all-roles`, { headers: this.getAuthHeaders() });
    }
  
}