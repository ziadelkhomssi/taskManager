import { Injectable } from '@angular/core';
import { NotificationControllerService, PageQuery, PageResponseNotificationDetails } from '../ng-openapi';
import { catchError, map, Observable, tap, throwError } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import { BaseApiService } from './base-api-service';

@Injectable({
  providedIn: 'root',
})
export class NotificationService extends BaseApiService {
  
  constructor(
    private notificationController: NotificationControllerService
  ) {
    super();
  }

  getDetailsList(query: PageQuery): Observable<PageResponseNotificationDetails> {
    return this.notificationController
      .getDetailsList1(query, "body")
      .pipe(
        tap(response => {
          console.log("Notifications fetched successfully!");
        }),
        map(response => response),
        catchError(this.handleError)
      );
  }

  markAsRead(notificationId: number): Observable<any> {
    return this.notificationController
      .markAsRead(notificationId, "body")
      .pipe(
        tap(() => {
          console.log(`Notification ${notificationId} marked as read`);
        }),
        catchError(this.handleError)
      );
  }
}
