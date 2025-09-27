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
  profile: Partial<UserItem> & { password?: string; confirmPassword?: string } = {};
  originalProfile: Partial<UserItem> = {};

  isLoadingBooks = false;
  isLoadingEvents = false;
  isLoadingProfile = false;
  isSavingProfile = false;

  // Error handling
  profileError = '';
  booksError = '';
  eventsError = '';

  // Success messages
  profileSuccess = '';
  booksSuccess = '';
  eventsSuccess = '';

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
    this.booksError = '';
    this.borrowingService.getMyBorrowings().subscribe({
      next: (list) => { 
        this.myBorrowings = list; 
        this.isLoadingBooks = false; 
      },
      error: (err) => { 
        this.booksError = 'Failed to load your borrowings. Please try again.'; 
        this.isLoadingBooks = false; 
        console.error('Error loading borrowings:', err);
      }
    });
  }

  loadEvents(): void {
    this.isLoadingEvents = true;
    this.eventsError = '';
    this.eventService.myRegistrations().subscribe({
      next: (list) => { 
        this.myRegistrations = list; 
        this.isLoadingEvents = false; 
      },
      error: (err) => { 
        this.eventsError = 'Failed to load your event registrations. Please try again.'; 
        this.isLoadingEvents = false; 
        console.error('Error loading events:', err);
      }
    });
  }

  loadProfile(): void {
    this.isLoadingProfile = true;
    this.profileError = '';
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
        this.originalProfile = { ...this.profile };
        this.isLoadingProfile = false;
      },
      error: (err) => {
        this.profileError = 'Failed to load your profile. Please try again.';
        this.isLoadingProfile = false;
        console.error('Error loading profile:', err);
      }
    });
  }

  saveProfile(): void {
    if (this.isSavingProfile) return;
    
    // Validate form
    if (!this.profile.firstName?.trim()) {
      this.profileError = 'First name is required';
      return;
    }
    
    if (!this.profile.lastName?.trim()) {
      this.profileError = 'Last name is required';
      return;
    }
    
    if (!this.profile.email?.trim()) {
      this.profileError = 'Email is required';
      return;
    }
    
    // Simple email validation
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(this.profile.email)) {
      this.profileError = 'Please enter a valid email address';
      return;
    }

    // Check if password fields are filled and match
    if (this.profile.password || this.profile.confirmPassword) {
      if (this.profile.password !== this.profile.confirmPassword) {
        this.profileError = 'Passwords do not match';
        return;
      }
      
      if (this.profile.password && this.profile.password.length < 6) {
        this.profileError = 'Password must be at least 6 characters long';
        return;
      }
    }

    this.isSavingProfile = true;
    this.profileError = '';
    this.profileSuccess = '';

    const updateData: Partial<UserItem> & { password?: string } = {
      firstName: this.profile.firstName,
      lastName: this.profile.lastName,
      email: this.profile.email,
      phoneNumber: this.profile.phoneNumber
    };

    // Only include password if it's filled
    if (this.profile.password) {
      updateData.password = this.profile.password;
    }

    this.userService.updateProfile(updateData).subscribe({
      next: () => {
        this.profileSuccess = 'Profile updated successfully!';
        this.isSavingProfile = false;
        this.originalProfile = { ...this.profile };
        
        // Clear password fields after successful update
        this.profile.password = '';
        this.profile.confirmPassword = '';
        
        // Clear success message after 3 seconds
        setTimeout(() => {
          this.profileSuccess = '';
        }, 3000);
      },
      error: (err) => {
        this.profileError = 'Failed to update profile. Please try again.';
        this.isSavingProfile = false;
        console.error('Error updating profile:', err);
      }
    });
  }

  returnBook(borrowingId: number): void {
    const borrowing = this.myBorrowings.find(b => b.id === borrowingId);
    if (!borrowing || borrowing.status !== 'ACTIVE') return;

    if (confirm('Are you sure you want to return this book?')) {
      this.borrowingService.returnBook(borrowingId).subscribe({
        next: () => {
          this.booksSuccess = 'Book returned successfully!';
          this.loadBooks();
          
          // Clear success message after 3 seconds
          setTimeout(() => {
            this.booksSuccess = '';
          }, 3000);
        },
        error: (err) => {
          this.booksError = 'Failed to return book. Please try again.';
          console.error('Error returning book:', err);
        }
      });
    }
  }

  unregisterEvent(eventId: number): void {
    const registration = this.myRegistrations.find(r => r.event?.id === eventId);
    if (!registration) return;

    if (confirm(`Are you sure you want to unregister from "${registration.event?.title}"?`)) {
      this.eventService.unregister(eventId).subscribe({
        next: () => {
          this.eventsSuccess = 'Successfully unregistered from event!';
          this.loadEvents();
          
          // Clear success message after 3 seconds
          setTimeout(() => {
            this.eventsSuccess = '';
          }, 3000);
        },
        error: (err) => {
          this.eventsError = 'Failed to unregister from event. Please try again.';
          console.error('Error unregistering from event:', err);
        }
      });
    }
  }

  showTab(tab: 'info' | 'books' | 'events'): void {
    this.activeTab = tab;
    // Clear messages when switching tabs
    this.profileError = '';
    this.profileSuccess = '';
    this.booksError = '';
    this.booksSuccess = '';
    this.eventsError = '';
    this.eventsSuccess = '';
  }

  // Helper method to check if profile has unsaved changes
  hasUnsavedChanges(): boolean {
    return JSON.stringify(this.profile) !== JSON.stringify(this.originalProfile);
  }

  // Helper method to get status badge class for books
  getBookStatusClass(status: string): string {
    switch (status) {
      case 'ACTIVE': return 'ACTIVE';
      case 'OVERDUE': return 'OVERDUE';
      case 'RETURNED': return 'RETURNED';
      default: return 'available';
    }
  }

  // Helper method to get event status
  getEventStatus(event: any): string {
    const eventDate = new Date(event.event?.startDate);
    const now = new Date();
    
    if (eventDate > now) return 'upcoming';
    if (eventDate.toDateString() === now.toDateString()) return 'ongoing';
    return 'past';
  }

  // Helper method to format date
  formatDate(dateString: string | undefined): string {
    if (!dateString) return '-';
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    });
  }

  // Helper method to check if book can be returned
  canReturnBook(borrowing: BorrowingItem): boolean {
    return borrowing.status === 'ACTIVE';
  }

  // Helper method to check if event can be unregistered
  canUnregisterEvent(event: any): boolean {
    const eventDate = new Date(event.event?.startDate);
    const now = new Date();
    return eventDate > now;
  }
}
