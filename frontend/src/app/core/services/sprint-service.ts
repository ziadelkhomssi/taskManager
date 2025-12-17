import { Injectable } from '@angular/core';
import { BaseApiService } from './base-api-service';
import { PageSprintSummary, SprintCreate, SprintDetails, SprintUpdate } from '../ng-openapi';
import { catchError, Observable } from 'rxjs';
import { HttpParams } from '@angular/common/http';
import { PageQuery } from '../../shared/component/entity-table/entity-table';

@Injectable({
  providedIn: 'root',
})
export class SprintService extends BaseApiService {
  private readonly sprintUrl = `${this.BASE_URL}/sprint`;

  getSummaryList(query: PageQuery): Observable<PageSprintSummary> {
    const params = new HttpParams({ fromObject: query as any });

    return this.http
      .get<PageSprintSummary>(`${this.sprintUrl}/summary`, { params })
      .pipe(catchError(this.handleError));
  }

  getDetailsById(id: number): Observable<SprintDetails> {
    return this.http
      .get<SprintDetails>(`${this.sprintUrl}/details/${id}`)
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

