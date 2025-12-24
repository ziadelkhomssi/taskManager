import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { BaseApiService } from './base-api-service';
import { PageProjectSummary, PageSprintSummary, PageUserSummary, ProjectCreate, ProjectDetails, ProjectUpdate } from '../ng-openapi';
import { HttpParams } from '@angular/common/http';
import { PageQuery } from '../../shared/types/types';

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
      .get<PageUserSummary>(`${this.projectUrl}/${projectId}/participant/summary`, { params })
      .pipe(catchError(this.handleError));
  }

  getDetailsById(id: number): Observable<ProjectDetails> {
    return this.http
      .get<ProjectDetails>(`${this.projectUrl}/details/${id}`)
      .pipe(catchError(this.handleError));
  }

  create(formData: FormData): Observable<void> {
    return this.http
      .post<void>(`${this.projectUrl}/create`, formData)
      .pipe(catchError(this.handleError));
  }

  update(formData: FormData): Observable<void> {
    return this.http
      .put<void>(`${this.projectUrl}/update`, formData)
      .pipe(catchError(this.handleError));
  }

  deleteById(id: number): Observable<void> {
    return this.http
      .delete<void>(`${this.projectUrl}/delete/${id}`)
      .pipe(catchError(this.handleError));
  }

  getProfilePicture(id: number): Observable<File | null> {
    return this.http
      .get(`${this.projectUrl}/profile-picture/${id}?cacheBuster=${Date.now()}`, { responseType: 'blob' })
      .pipe(
        map(blob => {
          if (!blob || blob.size === 0 || blob.type.includes('html')) {
            return null;
          }
          const mimeType = blob.type || 'image/png';
          const extension = mimeType.split('/')[1];
          return new File([blob], `profilePicture.${extension}`, { type: mimeType });
        }),
        catchError(err => of(null))
      );
  }
}
