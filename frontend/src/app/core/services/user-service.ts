import { Injectable } from '@angular/core';
import { BaseApiService } from './base-api-service';
import { PageUserSummary, UserDetails, UserSummary } from '../ng-openapi';
import { catchError, Observable } from 'rxjs';
import { HttpParams } from '@angular/common/http';
import { PageQuery } from '../../shared/types/types';

@Injectable({
  providedIn: 'root',
})
export class UserService extends BaseApiService {
  private readonly userUrl = `${this.BASE_URL}/user`;

  getSummaryList(query: PageQuery): Observable<PageUserSummary> {
    const params = new HttpParams({ fromObject: query as any });

    return this.http
      .get<PageUserSummary>(`${this.userUrl}/summary`, { params })
      .pipe(catchError(this.handleError));
  }

  getDetailsById(id: string): Observable<UserDetails> {
    return this.http
          .get<UserDetails>(`${this.userUrl}/details/${id}`)
          .pipe(catchError(this.handleError));
  }

  getClientSummary(): Observable<UserSummary> {
    return this.http
          .get<UserSummary>(`${this.userUrl}/summary/client`)
          .pipe(catchError(this.handleError));
  }
}

