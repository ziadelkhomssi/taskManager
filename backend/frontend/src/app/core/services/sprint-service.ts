import { Injectable } from '@angular/core';
import { BaseApiService } from './base-api-service';
import { PageSprintSummary, PageTicketSummary, PageUserSummary, SprintCreate, SprintDetails, SprintUpdate } from '../ng-openapi';
import { catchError, Observable } from 'rxjs';
import { HttpParams } from '@angular/common/http';
import { PageQuery } from '../../shared/types/types';

@Injectable({
  providedIn: 'root',
})
export class SprintService extends BaseApiService {
  private readonly sprintUrl = `${this.BASE_URL}/sprint`;

  getDetailsById(id: number): Observable<SprintDetails> {
    return this.http
      .get<SprintDetails>(`${this.sprintUrl}/details/${id}`)
      .pipe(catchError(this.handleError));
  }

  getTicketSummaryList(
    sprintId: number,
    query: PageQuery
  ): Observable<PageTicketSummary> {
    const params = new HttpParams({ fromObject: query as any });
  
    return this.http
      .get<PageTicketSummary>(`${this.sprintUrl}/${sprintId}/ticket/summary`, { params })
      .pipe(catchError(this.handleError));
  }

  getUserSummaryList(
    sprintId: number,
    query: PageQuery
  ): Observable<PageUserSummary> {
    const params = new HttpParams({ fromObject: query as any });

    return this.http
      .get<PageUserSummary>(`${this.sprintUrl}/${sprintId}/participant/summary`, { params })
      .pipe(catchError(this.handleError));
  }

  create(command: SprintCreate): Observable<void> {
    return this.http
      .post<void>(`${this.sprintUrl}/create`, command)
      .pipe(catchError(this.handleError));
  }

  update(command: SprintUpdate): Observable<void> {
    return this.http
      .put<void>(`${this.sprintUrl}/update`, command)
      .pipe(catchError(this.handleError));
  }

  deleteById(id: number): Observable<void> {
    return this.http
      .delete<void>(`${this.sprintUrl}/delete/${id}`)
      .pipe(catchError(this.handleError));
  }
}

