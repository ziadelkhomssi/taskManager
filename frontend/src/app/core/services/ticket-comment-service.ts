import { Injectable } from '@angular/core';
import { BaseApiService } from './base-api-service';
import { PageQuery, PageResponseTicketCommentDetails, TicketCommentControllerService, TicketCommentCreate, TicketCommentUpdate } from '../ng-openapi';
import { catchError, Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class TicketCommentService extends BaseApiService {

  constructor(
    private ticketCommentController: TicketCommentControllerService
  ) {
    super();
  }

  getDetailsList(
    ticketId: number,
    query: PageQuery
  ): Observable<PageResponseTicketCommentDetails> {
    return this.ticketCommentController
      .getDetailsList(ticketId, query, 'body')
      .pipe(catchError(this.handleError));
  }

  create(command: TicketCommentCreate): Observable<any> {
    return this.ticketCommentController
      .createTicketComment(command, 'body')
      .pipe(catchError(this.handleError));
  }

  update(command: TicketCommentUpdate): Observable<any> {
    return this.ticketCommentController
      .updateTicketComment(command, 'body')
      .pipe(catchError(this.handleError));
  }

  deleteById(id: number): Observable<any> {
    return this.ticketCommentController
      .deleteTicketCommentById(id, 'body')
      .pipe(catchError(this.handleError));
  }
}
