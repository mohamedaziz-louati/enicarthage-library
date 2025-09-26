import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

export interface UserItem {
  id: number;
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  phoneNumber?: string;
  role: 'ADMIN' | 'LIBRARIAN' | 'STUDENT' | 'FACULTY';
  status: 'ACTIVE' | 'INACTIVE' | 'SUSPENDED';
  createdAt?: string;
}

export interface PagedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

@Injectable({ providedIn: 'root' })
export class UserService {
  private readonly API = `${environment.apiUrl}/users`;

  constructor(private http: HttpClient) {}

  getAll(page = 0, size = 20, sortBy = 'firstName', sortDir: 'asc' | 'desc' = 'asc'): Observable<PagedResponse<UserItem>> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sortBy', sortBy)
      .set('sortDir', sortDir);
    return this.http.get<PagedResponse<UserItem>>(this.API, { params });
  }

  getById(id: number): Observable<UserItem> {
    return this.http.get<UserItem>(`${this.API}/${id}`);
  }

  searchByName(name: string): Observable<UserItem[]> {
    const params = new HttpParams().set('name', name);
    return this.http.get<UserItem[]>(`${this.API}/search`, { params });
  }

  getByRole(role: string): Observable<UserItem[]> {
    return this.http.get<UserItem[]>(`${this.API}/role/${role}`);
  }

  update(id: number, payload: Partial<UserItem>): Observable<any> {
    return this.http.put(`${this.API}/${id}`, payload);
  }

  create(payload: Partial<UserItem> & { password: string }): Observable<any> {
    return this.http.post(this.API, payload);
  }

  updateStatus(id: number, status: string): Observable<any> {
    const params = new HttpParams().set('status', status);
    return this.http.patch(`${this.API}/${id}/status`, null, { params });
  }

  updateRole(id: number, role: string): Observable<any> {
    const params = new HttpParams().set('role', role);
    return this.http.patch(`${this.API}/${id}/role`, null, { params });
  }

  getProfile(): Observable<UserItem> {
    return this.http.get<UserItem>(`${this.API}/profile`);
  }

  updateProfile(payload: Partial<UserItem> & { password?: string }): Observable<any> {
    return this.http.put(`${this.API}/profile`, payload);
  }
}
