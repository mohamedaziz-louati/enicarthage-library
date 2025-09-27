import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UserService, UserItem } from '../../core/services/user.service';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [
    CommonModule, 
    FormsModule
  ],
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.scss']
})
export class UsersComponent implements OnInit {
  users: UserItem[] = [];
  searchTerm = '';
  selectedRole = '';
  selectedStatus = '';
  currentPage = 0;
  pageSize = 10;
  totalItems = 0;
  isLoading = false;
  isAdmin = false;
  
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
    private userService: UserService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.isAdmin = this.authService.hasRole('ADMIN');
    this.loadUsers();
  }

  loadUsers(): void {
    this.isLoading = true;
    this.userService.getAll(this.currentPage, this.pageSize).subscribe({
      next: (resp) => {
        this.users = resp.content || [];
        this.totalItems = resp.totalElements || 0;
        this.isLoading = false;
      },
      error: (err: any) => {
        this.isLoading = false;
        this.snackBar.open(err?.error?.error || 'Failed to load users', 'Close', { duration: 3000 });
      }
    });
  }

  onSearch(): void {
    this.currentPage = 0;
    this.loadUsers();
  }

  onRoleChange(): void {
    this.currentPage = 0;
    this.loadUsers();
  }

  onStatusChange(): void {
    this.currentPage = 0;
    this.loadUsers();
  }

  clearFilters(): void {
    this.searchTerm = '';
    this.selectedRole = '';
    this.selectedStatus = '';
    this.currentPage = 0;
    this.loadUsers();
  }

  createUser(): void {
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
      error: (err: any) => this.snackBar.open(err?.error?.error || 'Failed to create user', 'Close', { duration: 3000 })
    });
  }

  editUser(user: UserItem): void {
    // For now, just toggle between STUDENT and FACULTY as a simple edit
    const nextRole = user.role === 'STUDENT' ? 'FACULTY' : 'STUDENT';
    this.userService.updateRole(user.id, nextRole).subscribe({
      next: () => {
        this.snackBar.open(`Role updated to ${nextRole}`, 'Close', { duration: 2500 });
        this.loadUsers();
      },
      error: (err: any) => this.snackBar.open(err?.error?.error || 'Failed to update role', 'Close', { duration: 3000 })
    });
  }

  deleteUser(user: UserItem): void {
    if (confirm(`Are you sure you want to delete user "${user.firstName} ${user.lastName}"?`)) {
      this.userService.updateStatus(user.id, 'INACTIVE').subscribe({
        next: () => {
          this.snackBar.open('User deactivated successfully', 'Close', { duration: 3000 });
          this.loadUsers();
        },
        error: (err: any) => this.snackBar.open(err?.error?.error || 'Failed to deactivate user', 'Close', { duration: 3000 })
      });
    }
  }

  viewProfile(user: UserItem): void {
    this.snackBar.open(`Viewing profile for ${user.firstName} ${user.lastName}`, 'Close', { duration: 2000 });
  }

  getPaginatedUsers(): UserItem[] {
    return this.users;
  }

  onPageChange(event: any): void {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadUsers();
  }
}
