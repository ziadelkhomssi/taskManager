import { Injectable } from '@angular/core';
import { PageQuery, PageResponseTicketSummary, TicketControllerService, TicketCreate, TicketDetails, TicketUpdate } from '../ng-openapi';
import { BaseApiService } from './base-api-service';
import { catchError, Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class TicketService extends BaseApiService {

  constructor(
    private ticketController: TicketControllerService
  ) {
    super();
  }

  getSummaryList(query: PageQuery): Observable<PageResponseTicketSummary> {
    return this.ticketController
      .getSummaryList1(query, 'body')
      .pipe(catchError(this.handleError));
  }

  getDetailsById(id: number): Observable<TicketDetails> {
    return this.ticketController
      .getDetailsById1(id, 'body')
      .pipe(catchError(this.handleError));
  }

  createTicket(command: TicketCreate): Observable<any> {
    return this.ticketController
      .createTicket(command, 'body')
      .pipe(catchError(this.handleError));
  }

  updateTicket(command: TicketUpdate): Observable<any> {
    return this.ticketController
      .updateTicket(command, 'body')
      .pipe(catchError(this.handleError));
  }

  deleteById(id: number): Observable<any> {
    return this.ticketController
      .deleteTicketById(id, 'body')
      .pipe(catchError(this.handleError));
  }
}

