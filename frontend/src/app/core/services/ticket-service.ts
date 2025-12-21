import { Injectable } from '@angular/core';
import { PageTicketSummary, TicketCreate, TicketDetails, TicketUpdate } from '../ng-openapi';
import { BaseApiService } from './base-api-service';
import { catchError, Observable } from 'rxjs';
import { HttpParams } from '@angular/common/http';
import { PageQuery } from '../../shared/component/entity-table/entity-table';

@Injectable({
  providedIn: 'root',
})
export class TicketService extends BaseApiService {
  private readonly ticketUrl = `${this.BASE_URL}/ticket`;

  getDetailsById(id: number): Observable<TicketDetails> {
    return this.http
      .get<TicketDetails>(`${this.ticketUrl}/details/${id}`)
      .pipe(catchError(this.handleError));
  }

  create(command: TicketCreate): Observable<void> {
    return this.http
      .post<void>(`${this.ticketUrl}/create`, command)
      .pipe(catchError(this.handleError));
  }

  update(command: TicketUpdate): Observable<void> {
    return this.http
      .put<void>(`${this.ticketUrl}/update`, command)
      .pipe(catchError(this.handleError));
  }

  deleteById(id: number): Observable<void> {
    return this.http
      .delete<void>(`${this.ticketUrl}/delete/${id}`)
      .pipe(catchError(this.handleError));
  }
}

