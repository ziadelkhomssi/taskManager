import { inject, Injectable, makeStateKey, TransferState } from '@angular/core';
import { BaseApiService } from './base-api-service';
import { ClientDetails, PageUserSummary, UserDetails, UserSummary } from '../ng-openapi';
import { BehaviorSubject, catchError, Observable, of, tap } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { PageQuery } from '../../shared/types/types';


const CLIENT_DETAILS_KEY = makeStateKey<ClientDetails>("client-details");

@Injectable({
  providedIn: 'root',
})
export class UserService extends BaseApiService {
  private readonly userUrl = `${this.BASE_URL}/user`;
  private clientDetailsSubject = new BehaviorSubject<ClientDetails | null>(null);
  clientDetails$ = this.clientDetailsSubject.asObservable();

  constructor(
    private transferState: TransferState
  ) {
    super(inject(HttpClient));
  }

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

  getClientDetails(): Observable<ClientDetails> {
    const cached = this.clientDetailsSubject.value;

    if (cached) {
      return of(cached);
    }

    if (this.transferState.hasKey(CLIENT_DETAILS_KEY)) {
      const data = this.transferState.get(CLIENT_DETAILS_KEY, null as any);
      this.transferState.remove(CLIENT_DETAILS_KEY);
      this.clientDetailsSubject.next(data);
      return of(data);
    }
    
    return this.http
          .get<ClientDetails>(`${this.userUrl}/details/client`)
          .pipe(tap(data => {
            this.clientDetailsSubject.next(data);
            this.transferState.set(CLIENT_DETAILS_KEY, data);
          }))
          .pipe(catchError(this.handleError));
  }
}

