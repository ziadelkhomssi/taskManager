import { Injectable } from '@angular/core';
import { PageNotificationDetails } from '../ng-openapi';
import { catchError, map, Observable, tap, throwError } from 'rxjs';
import { HttpErrorResponse, HttpParams } from '@angular/common/http';
import { BaseApiService } from './base-api-service';
import { PageQuery } from '../../shared/types/types';

@Injectable({
  providedIn: 'root',
})
export class NotificationService extends BaseApiService {
  private readonly notificationUrl = `${this.BASE_URL}/notification`;

  getAllForClient(query: PageQuery): Observable<PageNotificationDetails> {
    const params = new HttpParams({ fromObject: query as any });

    return this.http
      .get<PageNotificationDetails>(`${this.notificationUrl}/details`, { params })
      .pipe(catchError(this.handleError));
  }

  getHasUnread(): Observable<boolean> {
    return this.http
      .get<boolean>(`${this.notificationUrl}/has-unread`)
      .pipe(catchError(this.handleError));
  }

  markAsRead(id: number): Observable<any> {
    return this.http
      .post<void>(`${this.notificationUrl}/read/${id}`, {})
      .pipe(catchError(this.handleError));
  }
}
