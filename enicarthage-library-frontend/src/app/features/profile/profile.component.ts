import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BorrowingService, BorrowingItem } from '../../core/services/borrowing.service';
import { EventService } from '../../core/services/event.service';
import { UserService, UserItem } from '../../core/services/user.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  activeTab: 'info' | 'books' | 'events' = 'info';

  myBorrowings: BorrowingItem[] = [];
  myRegistrations: any[] = [];
  profile: Partial<UserItem> & { password?: string } = {};

  isLoadingBooks = false;
  isLoadingEvents = false;
  isSavingProfile = false;

  constructor(
    private borrowingService: BorrowingService,
    private eventService: EventService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.loadProfile();
    this.loadBooks();
    this.loadEvents();
  }

  loadBooks(): void {
    this.isLoadingBooks = true;
    this.borrowingService.getMyBorrowings().subscribe({
      next: (list) => { this.myBorrowings = list; this.isLoadingBooks = false; },
      error: () => { this.isLoadingBooks = false; }
    });
  }

  loadEvents(): void {
    this.isLoadingEvents = true;
    this.eventService.myRegistrations().subscribe({
      next: (list) => { this.myRegistrations = list; this.isLoadingEvents = false; },
      error: () => { this.isLoadingEvents = false; }
    });
  }

  loadProfile(): void {
    this.userService.getProfile().subscribe({
      next: (user) => {
        this.profile = {
          username: user.username,
          email: user.email,
          firstName: user.firstName,
          lastName: user.lastName,
          phoneNumber: user.phoneNumber,
          role: user.role,
          status: user.status
        };
      }
    });
  }

  saveProfile(): void {
    this.isSavingProfile = true;
    this.userService.updateProfile(this.profile).subscribe({
      next: () => { this.isSavingProfile = false; },
      error: () => { this.isSavingProfile = false; }
    });
  }

  returnBook(borrowingId: number): void {
    this.borrowingService.returnBook(borrowingId).subscribe(() => this.loadBooks());
  }

  unregisterEvent(eventId: number): void {
    this.eventService.unregister(eventId).subscribe(() => this.loadEvents());
  }

  showTab(tab: 'info' | 'books' | 'events'): void {
    this.activeTab = tab;
  }
}
