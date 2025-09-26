import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { EventService, EventItem } from '../../../core/services/event.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-event-detail',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="card" *ngIf="event; else loadingTpl">
      <h2>{{ event.title }}</h2>
      <p>{{ event.description }}</p>
      <p><strong>Location:</strong> {{ event.location || '-' }}</p>
      <p><strong>Start:</strong> {{ event.startDate | date:'medium' }}</p>
      <p><strong>End:</strong> {{ event.endDate | date:'medium' }}</p>
      <p><strong>Status:</strong> {{ event.status }}</p>
      <button *ngIf="!isAdmin" (click)="register()">Reserve Spot</button>
      <button *ngIf="!isAdmin" (click)="unregister()">Cancel Reservation</button>
    </div>
    <ng-template #loadingTpl><div class="card"><p>Loading event...</p></div></ng-template>
  `
})
export class EventDetailComponent implements OnInit {
  id!: number;
  event: EventItem | null = null;
  isAdmin = false;
  constructor(
    private route: ActivatedRoute,
    private eventService: EventService,
    private authService: AuthService,
    private snackBar: MatSnackBar
  ) {}
  ngOnInit(): void {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.authService.currentUser$.subscribe(user => {
      this.isAdmin = !!user && ['ADMIN', 'LIBRARIAN'].includes(user.role as any);
    });
    this.load();
  }

  load(): void {
    this.eventService.getById(this.id).subscribe({
      next: ev => this.event = ev
    });
  }

  register(): void {
    this.eventService.register(this.id).subscribe({
      next: () => this.snackBar.open('Registered successfully', 'Close', { duration: 2500 }),
      error: (err) => this.snackBar.open(err?.error?.error || 'Failed to register', 'Close', { duration: 3000 })
    });
  }

  unregister(): void {
    this.eventService.unregister(this.id).subscribe({
      next: () => this.snackBar.open('Cancelled successfully', 'Close', { duration: 2500 }),
      error: (err) => this.snackBar.open(err?.error?.error || 'Failed to cancel', 'Close', { duration: 3000 })
    });
  }
}
