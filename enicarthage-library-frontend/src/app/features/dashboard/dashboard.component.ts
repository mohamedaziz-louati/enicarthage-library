import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { DashboardService } from '../../core/services/dashboard.service';
import { AuthService } from '../../core/services/auth.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  totalBooks = 0;
  totalUsers = 0;
  totalEvents = 0;
  activeBorrowings = 0;
  isAdmin = false;

  constructor(private dashboardService: DashboardService, private authService: AuthService) {}

  ngOnInit() {
    this.isAdmin = this.authService.hasAnyRole(['ADMIN', 'LIBRARIAN']);
    if (this.isAdmin) {
      this.dashboardService.getOverview().subscribe({
        next: (overview) => {
          this.totalBooks = overview.books?.totalBooks || 0;
          this.totalUsers = overview.users?.totalUsers || 0;
          this.totalEvents = Number((overview.events as any)?.totalEvents || 0);
          this.activeBorrowings = Number((overview.borrowings as any)?.activeBorrowings || 0);
        }
      });
    }
  }
}