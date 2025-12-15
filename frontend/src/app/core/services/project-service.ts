import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { BaseApiService } from './base-api-service';
import { PageQuery, PageResponseProjectSummary, ProjectControllerService, ProjectCreate, ProjectDetails, ProjectUpdate } from '../ng-openapi';

@Injectable({ providedIn: 'root' })
export class ProjectService extends BaseApiService {

  constructor(
    private projectController: ProjectControllerService
  ) {
    super();
  }

  getSummaryList(query: PageQuery): Observable<PageResponseProjectSummary> {
    return this.projectController
      .getSummaryList3(query, 'body')
      .pipe(catchError(this.handleError));
  }

  getDetailsById(id: number): Observable<ProjectDetails> {
    return this.projectController
      .getDetailsById3(id, 'body')
      .pipe(catchError(this.handleError));
  }

  create(command: ProjectCreate): Observable<any> {
    return this.projectController
      .createProject(command, 'body')
      .pipe(catchError(this.handleError));
  }

  update(command: ProjectUpdate): Observable<any> {
    return this.projectController
      .updateProject(command, 'body')
      .pipe(catchError(this.handleError));
  }

  deleteById(id: number): Observable<any> {
    return this.projectController
      .deleteProjectById(id, 'body')
      .pipe(catchError(this.handleError));
  }
}
