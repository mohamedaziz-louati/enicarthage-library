import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatChipsModule } from '@angular/material/chips';
import { MatPaginatorModule, PageEvent } from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { Book, BookCategory, BookStatus } from '../../core/models/book.model';
import { FormsModule } from '@angular/forms';
import { BookService, PagedResponse } from '../../core/services/book.service';
import { AuthService } from '../../core/services/auth.service';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-books',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatChipsModule,
    MatPaginatorModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './books.component.html',
  styleUrls: ['./books.component.scss']
})
export class BooksComponent implements OnInit {
  isAdmin = false;
  isAuthenticated = false;
  books: Book[] = [];
  filteredBooks: Book[] = [];
  isLoading = false;
  searchTerm = '';
  selectedCategory: BookCategory | null = null;
  selectedStatus: BookStatus | null = null;
  
  // Pagination
  pageSize = 12;
  currentPage = 0;
  totalItems = 0;
  
  // Categories for filter
  categories = Object.values(BookCategory);
  statuses = Object.values(BookStatus);
  newBook: Partial<Book> = {
    title: '',
    author: '',
    publisher: '',
    publicationYear: new Date().getFullYear(),
    category: BookCategory.FICTION,
    description: '',
    totalCopies: 1,
    availableCopies: 1
  };

  constructor(
    private bookService: BookService,
    private authService: AuthService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit() {
    this.authService.currentUser$.subscribe(user => {
      this.isAuthenticated = !!user;
      this.isAdmin = !!user && ['ADMIN', 'LIBRARIAN'].includes(user.role as any);
    });
    this.loadBooks();
  }

  loadBooks() {
    this.isLoading = true;
    // If there's a search term, use the search API; otherwise fetch paginated list
    const page = this.currentPage;
    const size = this.pageSize;

    const onSuccess = (resp: PagedResponse<Book>) => {
      this.books = resp.content;
      this.filteredBooks = this.applyLocalFilters(this.books);
      this.totalItems = resp.totalElements;
      this.isLoading = false;
    };

    const onError = () => { this.isLoading = false; };

    if (this.searchTerm && this.searchTerm.trim().length > 0) {
      this.bookService.search(this.searchTerm.trim(), { page, size }).subscribe({ next: onSuccess, error: onError });
    } else {
      this.bookService.getAll({ page, size }).subscribe({ next: onSuccess, error: onError });
    }
  }

  private applyLocalFilters(source: Book[]): Book[] {
    return source.filter(book => {
      const matchesCategory = !this.selectedCategory || book.category === this.selectedCategory;
      const matchesStatus = !this.selectedStatus || book.status === this.selectedStatus;
      return matchesCategory && matchesStatus;
    });
  }

  applyFilters() {
    // When filters change, reload current page and apply local filters for category/status
    this.currentPage = 0;
    this.loadBooks();
  }

  onSearch() {
    this.currentPage = 0;
    this.loadBooks();
  }

  onCategoryChange() {
    this.applyFilters();
  }

  onStatusChange() {
    this.applyFilters();
  }

  clearFilters() {
    this.searchTerm = '';
    this.selectedCategory = null;
    this.selectedStatus = null;
    this.currentPage = 0;
    this.loadBooks();
  }

  onPageChange(event: PageEvent) {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.loadBooks();
  }

  getPaginatedBooks(): Book[] {
    // Server returns current page; we already loaded it in books -> filteredBooks
    return this.filteredBooks;
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

  borrowBook(book: Book) {
    if (!this.isAuthenticated) {
      this.snackBar.open('Please login to borrow books', 'Close', { duration: 2500 });
      this.router.navigate(['/login']);
      return;
    }
    this.bookService.borrowBook(book.id).subscribe({
      next: () => {
        this.snackBar.open('Book borrowed successfully', 'Close', { duration: 2500 });
        this.loadBooks(); // Refresh the book list
      },
      error: (error) => {
        this.snackBar.open(error?.error?.error || 'Borrow failed', 'Close', { duration: 3000 });
      }
    });
  }

  reserveBook(book: Book) {
    if (!this.isAuthenticated) {
      this.snackBar.open('Please login to reserve books', 'Close', { duration: 2500 });
      this.router.navigate(['/login']);
      return;
    }
    this.bookService.reserveBook(book.id).subscribe({
      next: () => {
        this.snackBar.open('Book reserved successfully', 'Close', { duration: 2500 });
        this.loadBooks(); // Refresh the book list
      },
      error: (error) => {
        this.snackBar.open(error?.error?.error || 'Reservation failed', 'Close', { duration: 3000 });
      }
    });
  }

  deleteBook(book: Book): void {
    if (!this.isAdmin) return;
    if (!confirm(`Delete "${book.title}"?`)) return;
    this.bookService.deleteBook(book.id).subscribe({
      next: () => this.loadBooks()
    });
  }

  createBook(): void {
    if (!this.isAdmin) return;
    if (!this.newBook.title || !this.newBook.author || !this.newBook.publisher || !this.newBook.description) return;
    this.bookService.createBook(this.newBook).subscribe({
      next: () => {
        this.newBook = {
          title: '',
          author: '',
          publisher: '',
          publicationYear: new Date().getFullYear(),
          category: BookCategory.FICTION,
          description: '',
          totalCopies: 1,
          availableCopies: 1
        };
        this.loadBooks();
      }
    });
  }
}