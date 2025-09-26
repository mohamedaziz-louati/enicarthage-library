import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EventService, PagedResponse, EventItem } from '../../core/services/event.service';
import { AuthService } from '../../core/services/auth.service';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-events',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './events.component.html',
  styleUrls: ['./events.component.scss']
})
export class EventsComponent implements OnInit {
  events: EventItem[] = [];
  isLoading = false;

  searchTerm = '';
  selectedStatus: 'upcoming' | 'past' | 'cancelled' | 'all' = 'all';

  pageSize = 10;
  currentPage = 0;
  totalItems = 0;
  newEvent: Partial<EventItem> = {
    title: '',
    description: '',
    location: '',
    startDate: '',
    endDate: '',
    type: 'WORKSHOP',
    registrationRequired: true
  } as any;

  isAdmin = false;
  isAuthenticated = false;

  constructor(
    private eventService: EventService,
    private authService: AuthService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.authService.currentUser$.subscribe(user => {
      this.isAuthenticated = !!user;
      this.isAdmin = !!user && ['ADMIN', 'LIBRARIAN'].includes(user.role as any);
    });
    this.load();
  }

  load(): void {
    this.isLoading = true;
    const page = this.currentPage; const size = this.pageSize;

    const handleResp = (resp: PagedResponse<EventItem>) => {
      this.events = resp.content;
      this.totalItems = resp.totalElements;
      this.isLoading = false;
    };
    const handleErr = () => { this.isLoading = false; };

    // Use search endpoint when searchTerm is present
    if (this.searchTerm.trim()) {
      this.eventService.search(this.searchTerm.trim(), page, size).subscribe({ next: handleResp, error: handleErr });
    } else {
      // Sort by startDate ascending to mimic screenshot
      this.eventService.getAll(page, size, 'startDate', 'asc').subscribe({ next: handleResp, error: handleErr });
    }
  }

  onSearch(): void {
    this.currentPage = 0;
    this.load();
  }

  onStatusChange(): void {
    // Client-side filter on status for now since backend status filter is via endpoints
    // If needed, we could call specific endpoints (upcoming/ongoing) here
  }

  getPagedEvents(): EventItem[] {
    // Apply local status filter for UI parity
    let list = this.events;
    if (this.selectedStatus !== 'all') {
      list = list.filter(e => {
        if (this.selectedStatus === 'upcoming') return (e.status || '').toLowerCase() === 'upcoming';
        if (this.selectedStatus === 'past') {
          const status = (e.status || '').toLowerCase();
          return status === 'past' || status === 'completed';
        }
        if (this.selectedStatus === 'cancelled') return (e.status || '').toLowerCase() === 'cancelled';
        return true;
      });
    }
    return list;
  }

  onPagePrev(): void { if (this.currentPage > 0) { this.currentPage--; this.load(); } }
  onPageNext(): void { if ((this.currentPage + 1) * this.pageSize < this.totalItems) { this.currentPage++; this.load(); } }

  registerEvent(ev: EventItem): void {
    if (!this.isAuthenticated) {
      this.snackBar.open('Please login to reserve events', 'Close', { duration: 2500 });
      this.router.navigate(['/login']);
      return;
    }
    this.eventService.register(ev.id).subscribe({
      next: () => {
        this.snackBar.open('Registered successfully', 'Close', { duration: 2500 });
        this.load();
      },
      error: (err) => this.snackBar.open(err?.error?.error || 'Registration failed', 'Close', { duration: 3000 })
    });
  }

  unregisterEvent(ev: EventItem): void {
    if (!this.isAuthenticated) return;
    this.eventService.unregister(ev.id).subscribe({
      next: () => {
        this.snackBar.open('Registration cancelled', 'Close', { duration: 2500 });
        this.load();
      },
      error: (err) => this.snackBar.open(err?.error?.error || 'Cancellation failed', 'Close', { duration: 3000 })
    });
  }

  deleteEvent(ev: EventItem): void {
    if (!this.isAdmin) return;
    if (confirm('Delete this event?')) {
      this.eventService.remove(ev.id).subscribe(() => this.load());
    }
  }

  createEvent(): void {
    if (!this.isAdmin) return;
    if (!this.newEvent.title || !this.newEvent.description || !this.newEvent.startDate || !this.newEvent.endDate) return;
    this.eventService.create(this.newEvent).subscribe({
      next: () => {
        this.newEvent = {
          title: '',
          description: '',
          location: '',
          startDate: '',
          endDate: '',
          type: 'WORKSHOP',
          registrationRequired: true
        } as any;
        this.load();
      }
    });
  }
}
