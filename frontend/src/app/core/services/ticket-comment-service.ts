import { Injectable } from '@angular/core';
import { BaseApiService } from './base-api-service';
import { PageTicketCommentDetails, TicketCommentCreate, TicketCommentUpdate } from '../ng-openapi';
import { catchError, Observable } from 'rxjs';
import { PageQuery } from '../../shared/component/entity-table/entity-table';
import { HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class TicketCommentService extends BaseApiService {
  private readonly ticketCommentUrl = `${this.BASE_URL}/ticketComment`;

  getDetailsList(
    ticketId: number,
    query: PageQuery
  ): Observable<PageTicketCommentDetails> {
    const params = new HttpParams({ fromObject: query as any });

    return this.http
      .get<PageTicketCommentDetails>(`${this.ticketCommentUrl}/details`, { params })
  }

  create(command: TicketCommentCreate): Observable<void> {
    return this.http
      .post<void>(`${this.ticketCommentUrl}/create`, command)
      .pipe(catchError(this.handleError));
  }

  update(command: TicketCommentUpdate): Observable<void> {
    return this.http
      .put<void>(`${this.ticketCommentUrl}/update`, command)
      .pipe(catchError(this.handleError));
  }

  deleteById(id: number): Observable<void> {
    return this.http
      .delete<void>(`${this.ticketCommentUrl}/delete/${id}`)
      .pipe(catchError(this.handleError));
  }
}
