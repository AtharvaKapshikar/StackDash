import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Inject, Injectable, PLATFORM_ID } from '@angular/core';
import { Observable } from 'rxjs';
import { Task } from '../../dashboardmodel/task';
import { AddTask } from '../../dashboardmodel/add-task';
import { User } from '../../dashboardmodel/user';
import { RoleDto } from '../../dashboardmodel/roledto';
import { isPlatformBrowser } from '@angular/common';

@Injectable({
  providedIn: 'root'
})
export class AdminDashboardServiceService {
  private baseURL: string = 'http://localhost:8082/api/admin/';

  constructor(
    private http: HttpClient,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  // private getAuthHeaders(): HttpHeaders {
  //   const token = localStorage.getItem('token');
  //   return new HttpHeaders({
  //     'Authorization': `Bearer ${token}`
  //   });
  // }

  private getAuthHeaders(): HttpHeaders {
    let token = '';

    if (isPlatformBrowser(this.platformId)) {
      token = localStorage.getItem('token') || '';
    }

    return new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
  }

  getTaskCount(): Observable<any> {
    return this.http.get(`${this.baseURL}task/count`, { headers: this.getAuthHeaders() });
  }

  getDashboardUserCount(): Observable<any> {
    return this.http.get(`${this.baseURL}dashboard/user-count`, { headers: this.getAuthHeaders() });
  }

  getAllUserList(page: number, size: number): Observable<any> {
    const param = new HttpParams().set('page', page).set('size', size);
    return this.http.get(`${this.baseURL}getAllUsers`, { headers: this.getAuthHeaders(), params: param });
  }

  getLatestUserList(): Observable<any> {
    return this.http.get(`${this.baseURL}latestusers`, { headers: this.getAuthHeaders() });
  }

  getAllTasks(page: number, size: number): Observable<any> {
  return this.http.get(`${this.baseURL}all-task?page=${page}&size=${size}`,  {
      headers: this.getAuthHeaders(),
      responseType: 'text'
    });
  }

  

  createTask(newTask: AddTask): Observable<any> {
    return this.http.post(`${this.baseURL}assign-task`, newTask, {
      headers: this.getAuthHeaders(),
      responseType: 'text'
    });
  }

  editTask(task: Task): Observable<any> {
    return this.http.put(`${this.baseURL}update-task`, task, {
      headers: this.getAuthHeaders(),
      responseType: 'text'
    });
  }

  getTask(taskId: number): Observable<any> {
    return this.http.get(`${this.baseURL}task/${taskId}`, { headers: this.getAuthHeaders() });
  }

  deleteTask(taskId: number): Observable<any> {
    return this.http.delete(`${this.baseURL}task/delete/${taskId}`, {
      headers: this.getAuthHeaders(),
      responseType: 'text'
    });
  }

  getUser(userId: number): Observable<any> {
    return this.http.get(`${this.baseURL}user/${userId}`, { headers: this.getAuthHeaders() });
  }

  getAllRoles(): Observable<RoleDto[]> {
    return this.http.get<RoleDto[]>(`${this.baseURL}all-roles`, { headers: this.getAuthHeaders() });
  }

  updateUser(user: User): Observable<any> {
    return this.http.put(`${this.baseURL}update/user`, user, {
      headers: this.getAuthHeaders(),
      responseType: 'text'
    });
  }

  deleteUser(userId: Number): Observable<any> {
    return this.http.delete(`${this.baseURL}delete/${userId}`, {
      headers: this.getAuthHeaders(),
      responseType: 'text'
    });
  }

}