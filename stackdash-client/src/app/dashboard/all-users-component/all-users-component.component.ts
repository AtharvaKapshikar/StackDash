import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { AdminDashboardServiceService } from '../admin-dashboard/admin-dashboard-service.service';
import { User } from '../../dashboardmodel/user';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-all-users-component',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterOutlet, FormsModule],
  templateUrl: './all-users-component.component.html',
  styleUrl: './all-users-component.component.scss'
})
export class AllUsersComponentComponent implements OnInit {

  users: User[] = [];
  filteredUsers: User[] = [];
  isLoading = true;
  errorMsg = '';
  message: string | null = null;
  messageType: 'success' | 'error' = 'success';
  searchTerm = '';
  totalPages = 0;
  pageSize = 10;
  currentPage = 0;

  constructor(private service: AdminDashboardServiceService, private router: Router) {}

  ngOnInit() {
    this.loadUsers(this.currentPage);
  }

  loadUsers(page: number = 0) {
  this.isLoading = true;
  this.service.getAllUserList(page, this.pageSize).subscribe({
    next: (response) => {
        let parsedResponse: any;

      try {
        parsedResponse = typeof response === 'string' ? JSON.parse(response) : response;
      } catch (err) {
        console.error('❌ Failed to parse users JSON:', err);
        this.users = [];
        this.filteredUsers = [];
        this.isLoading = false;
        return;
      }

      this.users = Array.isArray(parsedResponse) ? parsedResponse : parsedResponse.content ?? [];
      this.filteredUsers = [...this.users];
      this.totalPages = parsedResponse.totalPages ?? 1;
      this.currentPage = parsedResponse.number ?? 0;
      this.isLoading = false;

      console.log("✅ Parsed users:", this.users);
    },
    error: (error) => {
      this.errorMsg = error.message || 'Failed to load users';
      this.isLoading = false;
      console.error("❌ Error fetching users:", error);
    }
  });

}

  editUser(user: User) {
    this.router.navigate([`admin/edit/user/${user.userId}`]);
  }

  deleteUser(userId: number) {
    this.service.deleteUser(userId).subscribe({
      next: () => {
        this.message = '✅ User Deleted successfully!';
        this.messageType = 'success';
        setTimeout(() => this.message = null, 3000);
        this.loadUsers(this.currentPage); // refresh list
      },
      error: (err) => {
        this.message = err.error;
        this.messageType = 'error';
        setTimeout(() => this.message = null, 3000);
      }
    });
  }

  filterUsers() {
  const term = (this.searchTerm ?? '').trim().toLowerCase();

  this.filteredUsers = term
    ? this.users.filter(user =>
        ((user.firstName ?? '') + ' ' + (user.lastName ?? '')).toLowerCase().includes(term) ||
        String(user.userId ?? '').includes(term) ||
        (user.email ?? '').toLowerCase().includes(term) ||
        (user.city ?? '').toLowerCase().includes(term)
      )
    : [...this.users];
}

  getRoleClass(role: string): string {
    switch (role) {
      case 'ADMIN': return 'role-admin';
      case 'USER': return 'role-user';
      case 'MODERATOR': return 'role-moderator';
      default: return 'role-default';
    }
  }
}