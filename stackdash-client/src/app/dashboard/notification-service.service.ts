import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NotificationServiceService {

  constructor(private http: HttpClient) {}

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
  "Authorization": `Bearer ${token}`
});
  }

  getUserNotifications(userId: number): Observable<any> {
    console.log("from notify service:: calling api", this.getAuthHeaders);
    const url = `http://localhost:8082/api/notifications/${userId}`;
    return this.http.get(url, { headers: this.getAuthHeaders() });
  }
}