import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router, NavigationEnd } from '@angular/router';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { HeaderComponent } from './core/components/header/header.component';
import { SidebarComponent } from './core/components/sidebar/sidebar.component';
import { filter } from 'rxjs/operators';
import { AuthService } from './core/services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterModule, MatSnackBarModule, HeaderComponent, SidebarComponent],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'ENICARTHAGE Library Management System';
  showHeader = true;
  authRoutes = ['/login', '/register'];
  isAdminUser = false;

  constructor(private router: Router, private authService: AuthService) {}

  ngOnInit() {
    this.authService.currentUser$.subscribe(user => {
      this.isAdminUser = !!user && ['ADMIN', 'LIBRARIAN'].includes(user.role as any);
    });
  }

  // Check if current route is auth route
  isAuthRoute(): boolean {
    return this.authRoutes.some(route => this.router.url.includes(route));
  }

  // Check if current route is admin route
  showSidebar(): boolean {
    return !this.isAuthRoute() && this.isAdminUser;
  }
}