import { Injectable } from '@angular/core';
import { BaseApiService } from './base-api-service';
import { PageQuery, PageResponseUserSummary, UserControllerService, UserDetails } from '../ng-openapi';
import { catchError, Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class UserService extends BaseApiService {

  constructor(
    private userController: UserControllerService
  ) {
    super();
  }

  getSummaryList(query: PageQuery): Observable<PageResponseUserSummary> {
    return this.userController
      .getSummaryList(query, 'body')
      .pipe(catchError(this.handleError));
  }

  getSprintParticipants(
    sprintId: number,
    query: PageQuery
  ): Observable<PageResponseUserSummary> {
    return this.userController
      .getSprintParticipants(sprintId, query, 'body')
      .pipe(catchError(this.handleError));
  }

  getProjectParticipants(
    projectId: number,
    query: PageQuery
  ): Observable<PageResponseUserSummary> {
    return this.userController
      .getProjectParticipants(projectId, query, 'body')
      .pipe(catchError(this.handleError));
  }

  getDetailsById(id: string): Observable<UserDetails> {
    return this.userController
      .getDetailsById(id, 'body')
      .pipe(catchError(this.handleError));
  }
}

