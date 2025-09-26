import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatChipsModule } from '@angular/material/chips';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { Book, BookStatus, BookCategory } from '../../../core/models/book.model';
import { BookService } from '../../../core/services/book.service';

@Component({
  selector: 'app-book-detail',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatChipsModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './book-detail.component.html',
  styleUrls: ['./book-detail.component.scss']
})
export class BookDetailComponent implements OnInit {
  book: Book | null = null;
  isLoading = true;
  
  constructor(
    private route: ActivatedRoute,
    private bookService: BookService
  ) {}
  
  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) {
      this.loadBook(id);
    }
  }
  
  loadBook(id: number): void {
    this.bookService.getById(id).subscribe({
      next: (book) => {
        this.book = book;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading book:', error);
        this.isLoading = false;
      }
    });
  }
  
  borrowBook(): void {
    if (this.book) {
      this.bookService.borrowBook(this.book.id).subscribe({
        next: (response) => {
          console.log('Book borrowed successfully:', response);
          // Update book status locally
          if (this.book) {
            this.book.status = 'BORROWED' as BookStatus;
            this.book.availableCopies = Math.max(0, this.book.availableCopies - 1);
          }
        },
        error: (error) => {
          console.error('Error borrowing book:', error);
        }
      });
    }
  }
  
  reserveBook(): void {
    if (this.book) {
      this.bookService.reserveBook(this.book.id).subscribe({
        next: (response) => {
          console.log('Book reserved successfully:', response);
          // Update book status locally
          if (this.book) {
            this.book.status = 'RESERVED' as BookStatus;
            this.book.availableCopies = Math.max(0, this.book.availableCopies - 1);
          }
        },
        error: (error) => {
          console.error('Error reserving book:', error);
        }
      });
    }
  }
  
  getCategoryColor(category: string): string {
    const colors: { [key: string]: string } = {
      'FICTION': 'primary',
      'SCIENCE': 'accent',
      'TECHNOLOGY': 'primary',
      'HISTORY': 'warn',
      'LITERATURE': 'accent',
      'PHILOSOPHY': 'warn',
      'REFERENCE': 'primary'
    };
    return colors[category] || 'primary';
  }
  
  getStatusColor(status: BookStatus): string {
    const colors: { [key in BookStatus]: string } = {
      'AVAILABLE': 'primary',
      'BORROWED': 'accent',
      'RESERVED': 'warn',
      'MAINTENANCE': 'warn',
      'LOST': 'warn',
      'DAMAGED': 'warn'
    };
    return colors[status] || 'primary';
  }
}
