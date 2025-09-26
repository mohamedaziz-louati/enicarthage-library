import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UserService, UserItem } from '../../core/services/user.service';

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [
    CommonModule, 
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.scss']
})
export class UsersComponent implements OnInit {
  users: UserItem[] = [];
  filteredUsers: UserItem[] = [];
  searchTerm = '';
  selectedRole = '';
  currentPage = 0;
  totalPages = 1;
  pageSize = 20;
  isLoading = false;
  newUser: Partial<UserItem> & { password?: string } = {
    firstName: '',
    lastName: '',
    username: '',
    email: '',
    role: 'STUDENT',
    status: 'ACTIVE',
    password: ''
  };

  constructor(
    private snackBar: MatSnackBar,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.isLoading = true;
    this.userService.getAll(this.currentPage, this.pageSize).subscribe({
      next: (resp) => {
        this.users = resp.content;
        this.totalPages = resp.totalPages || 1;
        this.applyFilters();
        this.isLoading = false;
      },
      error: (err) => {
        this.isLoading = false;
        this.snackBar.open(err?.error?.error || 'Failed to load users', 'Close', { duration: 3000 });
      }
    });
  }

  onSearch(): void {
    this.applyFilters();
  }

  onFilterChange(): void {
    this.applyFilters();
  }

  applyFilters(): void {
    this.filteredUsers = this.users.filter(user => {
      const matchesSearch = !this.searchTerm || 
        user.firstName.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        user.lastName.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
        user.email.toLowerCase().includes(this.searchTerm.toLowerCase());
      
      const matchesRole = !this.selectedRole || user.role === this.selectedRole;
      
      return matchesSearch && matchesRole;
    });
  }

  openAddUserDialog(): void {
    if (!this.newUser.firstName || !this.newUser.lastName || !this.newUser.username || !this.newUser.email || !this.newUser.password) {
      this.snackBar.open('Fill all required fields to add user', 'Close', { duration: 2500 });
      return;
    }
    this.userService.create(this.newUser as any).subscribe({
      next: () => {
        this.snackBar.open('User created successfully', 'Close', { duration: 2500 });
        this.newUser = {
          firstName: '',
          lastName: '',
          username: '',
          email: '',
          role: 'STUDENT',
          status: 'ACTIVE',
          password: ''
        };
        this.loadUsers();
      },
      error: (err) => this.snackBar.open(err?.error?.error || 'Failed to create user', 'Close', { duration: 3000 })
    });
  }

  editUser(user: UserItem): void {
    const nextRole = user.role === 'STUDENT' ? 'FACULTY' : 'STUDENT';
    this.userService.updateRole(user.id, nextRole).subscribe({
      next: () => {
        this.snackBar.open(`Role updated to ${nextRole}`, 'Close', { duration: 2500 });
        this.loadUsers();
      },
      error: (err) => this.snackBar.open(err?.error?.error || 'Failed to update role', 'Close', { duration: 3000 })
    });
  }

  deleteUser(userId: number): void {
    if (confirm('Are you sure you want to delete this user?')) {
      this.userService.updateStatus(userId, 'INACTIVE').subscribe({
        next: () => {
          this.snackBar.open('User deactivated successfully', 'Close', { duration: 3000 });
          this.loadUsers();
        },
        error: (err) => this.snackBar.open(err?.error?.error || 'Failed to deactivate user', 'Close', { duration: 3000 })
      });
    }
  }

  getRoleClass(role: string): string {
    const roleClasses: { [key: string]: string } = {
      'ADMIN': 'admin',
      'LIBRARIAN': 'librarian',
      'STUDENT': 'student',
      'FACULTY': 'faculty'
    };
    return roleClasses[role] || 'student';
  }

  previousPage(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.loadUsers();
    }
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
      this.loadUsers();
    }
  }
}
