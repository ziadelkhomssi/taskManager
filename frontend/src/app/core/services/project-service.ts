import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { BaseApiService } from './base-api-service';
import { PageProjectSummary, PageSprintSummary, PageUserSummary, ProjectCreate, ProjectDetails, ProjectUpdate } from '../ng-openapi';
import { HttpParams } from '@angular/common/http';
import { PageQuery } from '../../shared/component/entity-table/entity-table';

@Injectable({
  providedIn: 'root',
})
export class ProjectService extends BaseApiService {
  private readonly projectUrl = `${this.BASE_URL}/project`;

  getSummaryList(query: PageQuery): Observable<PageProjectSummary> {
    const params = new HttpParams({ fromObject: query as any });

    return this.http
      .get<PageProjectSummary>(`${this.projectUrl}/summary`, { params })
      .pipe(catchError(this.handleError));
  }

  getSprintSummaryList(
    projectId: number,
    query: PageQuery
  ): Observable<PageSprintSummary> {
    const params = new HttpParams({ fromObject: query as any });
  
    return this.http
      .get<PageSprintSummary>(`${this.projectUrl}/${projectId}/sprint/summary`, { params })
      .pipe(catchError(this.handleError));
  }

  getUserSummaryList(
    projectId: number,
    query: PageQuery
  ): Observable<PageUserSummary> {
    const params = new HttpParams({ fromObject: query as any });

    return this.http
      .get<PageUserSummary>(`${this.projectUrl}${projectId}/participant/summary`, { params })
      .pipe(catchError(this.handleError));
  }

  getDetailsById(id: number): Observable<ProjectDetails> {
    return this.http
      .get<ProjectDetails>(`${this.projectUrl}/details/${id}`)
      .pipe(catchError(this.handleError));
  }

  create(command: ProjectCreate): Observable<void> {
    return this.http
      .post<void>(`${this.projectUrl}/create`, command)
      .pipe(catchError(this.handleError));
  }

  update(command: ProjectUpdate): Observable<void> {
    return this.http
      .put<void>(`${this.projectUrl}/update`, command)
      .pipe(catchError(this.handleError));
  }

  deleteById(id: number): Observable<void> {
    return this.http
      .delete<void>(`${this.projectUrl}/delete/${id}`)
      .pipe(catchError(this.handleError));
  }
}
